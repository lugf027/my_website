package io.lugf027.github.mywebsite.repository

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.DailyStatistics
import io.lugf027.github.mywebsite.dto.DeviceStatistics
import io.lugf027.github.mywebsite.dto.ReferrerStatistics
import io.lugf027.github.mywebsite.model.entities.AccessLog
import io.lugf027.github.mywebsite.model.tables.AccessLogs
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 访问日志数据访问层
 */
class AccessLogRepository : Logger() {
    
    /**
     * 记录访问日志
     */
    fun log(
        path: String,
        method: String,
        ip: String,
        userAgent: String?,
        referer: String?,
        userId: Long?,
        statusCode: Int,
        duration: Long
    ): AccessLog = transaction {
        AccessLog.new {
            this.path = path
            this.method = method
            this.ip = ip
            this.userAgent = userAgent
            this.referer = referer
            this.userId = userId
            this.statusCode = statusCode
            this.duration = duration
            this.createdAt = LocalDateTime.now()
        }
    }
    
    /**
     * 获取总 PV
     */
    fun getTotalPv(): Long = transaction {
        AccessLog.count()
    }
    
    /**
     * 获取今日 PV
     */
    fun getTodayPv(): Long = transaction {
        val today = LocalDate.now().atStartOfDay()
        AccessLog.find { AccessLogs.createdAt greaterEq today }.count()
    }
    
    /**
     * 获取总 UV（按 IP 去重）
     */
    fun getTotalUv(): Long = transaction {
        AccessLogs
            .select(AccessLogs.ip)
            .withDistinct()
            .count()
    }
    
    /**
     * 获取今日 UV
     */
    fun getTodayUv(): Long = transaction {
        val today = LocalDate.now().atStartOfDay()
        AccessLogs
            .select(AccessLogs.ip)
            .where { AccessLogs.createdAt greaterEq today }
            .withDistinct()
            .count()
    }
    
    /**
     * 获取每日统计数据
     */
    fun getDailyStats(startDate: LocalDate, endDate: LocalDate): List<DailyStatistics> = transaction {
        val start = startDate.atStartOfDay()
        val end = endDate.plusDays(1).atStartOfDay()
        
        // 获取日期范围内的数据
        val logs = AccessLog.find {
            (AccessLogs.createdAt greaterEq start) and (AccessLogs.createdAt lessEq end)
        }.toList()
        
        // 按日期分组统计
        val groupedByDate = logs.groupBy { it.createdAt.toLocalDate() }
        
        val result = mutableListOf<DailyStatistics>()
        var current = startDate
        while (!current.isAfter(endDate)) {
            val dayLogs = groupedByDate[current] ?: emptyList()
            result.add(DailyStatistics(
                date = current.format(DateTimeFormatter.ISO_LOCAL_DATE),
                pv = dayLogs.size.toLong(),
                uv = dayLogs.distinctBy { it.ip }.size.toLong()
            ))
            current = current.plusDays(1)
        }
        
        result
    }
    
    /**
     * 获取访问来源统计
     */
    fun getReferrerStats(limit: Int = 10): List<ReferrerStatistics> = transaction {
        AccessLogs
            .select(AccessLogs.referer, AccessLogs.id.count())
            .where { AccessLogs.referer.isNotNull() }
            .groupBy(AccessLogs.referer)
            .orderBy(AccessLogs.id.count() to SortOrder.DESC)
            .limit(limit)
            .map {
                ReferrerStatistics(
                    referrer = it[AccessLogs.referer] ?: "Direct",
                    count = it[AccessLogs.id.count()]
                )
            }
    }
    
    /**
     * 获取设备类型统计
     */
    fun getDeviceStats(): List<DeviceStatistics> = transaction {
        val logs = AccessLog.all().toList()
        
        val deviceCounts = logs.groupBy { log ->
            val ua = log.userAgent?.lowercase() ?: ""
            when {
                ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") -> "Mobile"
                ua.contains("tablet") || ua.contains("ipad") -> "Tablet"
                else -> "Desktop"
            }
        }.mapValues { it.value.size.toLong() }
        
        deviceCounts.map { (device, count) ->
            DeviceStatistics(device = device, count = count)
        }.sortedByDescending { it.count }
    }
    
    /**
     * 清理旧日志（保留指定天数）
     */
    fun cleanOldLogs(daysToKeep: Int): Int = transaction {
        val cutoff = LocalDateTime.now().minusDays(daysToKeep.toLong())
        AccessLogs.deleteWhere { createdAt lessEq cutoff }.also {
            if (it > 0) {
                logger.info("Cleaned {} old access logs", it)
            }
        }
    }
}
