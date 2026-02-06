package io.lugf027.github.mywebsite.service

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.*
import io.lugf027.github.mywebsite.repository.AccessLogRepository
import io.lugf027.github.mywebsite.repository.BlogRepository
import io.lugf027.github.mywebsite.repository.UserRepository
import java.time.LocalDate

/**
 * 统计服务
 */
class StatisticsService(
    private val accessLogRepository: AccessLogRepository = AccessLogRepository(),
    private val blogRepository: BlogRepository = BlogRepository(),
    private val userRepository: UserRepository = UserRepository()
) : Logger() {
    
    /**
     * 获取统计概览
     */
    fun getOverview(): StatisticsOverview {
        return StatisticsOverview(
            totalPv = accessLogRepository.getTotalPv(),
            todayPv = accessLogRepository.getTodayPv(),
            totalUv = accessLogRepository.getTotalUv(),
            todayUv = accessLogRepository.getTodayUv(),
            totalBlogs = blogRepository.count().toInt(),
            publishedBlogs = blogRepository.count(BlogStatus.PUBLISHED).toInt(),
            totalUsers = userRepository.count().toInt()
        )
    }
    
    /**
     * 获取完整统计报表
     */
    fun getReport(startDate: String?, endDate: String?): StatisticsReport {
        val start = startDate?.let { LocalDate.parse(it) } ?: LocalDate.now().minusDays(30)
        val end = endDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
        
        return StatisticsReport(
            overview = getOverview(),
            dailyStats = accessLogRepository.getDailyStats(start, end),
            popularBlogs = getPopularBlogs(),
            referrers = accessLogRepository.getReferrerStats(),
            devices = accessLogRepository.getDeviceStats()
        )
    }
    
    /**
     * 获取热门博客
     */
    private fun getPopularBlogs(limit: Int = 10): List<PopularBlog> {
        return blogRepository.findPopular(limit).map {
            PopularBlog(
                id = it.id.value,
                title = it.title,
                viewCount = it.viewCount
            )
        }
    }
    
    /**
     * 记录访问日志
     */
    fun logAccess(
        path: String,
        method: String,
        ip: String,
        userAgent: String?,
        referer: String?,
        userId: Long?,
        statusCode: Int,
        duration: Long
    ) {
        // 过滤不需要记录的路径
        if (shouldSkipLogging(path)) {
            return
        }
        
        accessLogRepository.log(
            path = path,
            method = method,
            ip = ip,
            userAgent = userAgent,
            referer = referer,
            userId = userId,
            statusCode = statusCode,
            duration = duration
        )
    }
    
    /**
     * 判断是否需要跳过日志记录
     */
    private fun shouldSkipLogging(path: String): Boolean {
        val skipPatterns = listOf(
            "/health",
            "/favicon.ico",
            "/robots.txt",
            "/.well-known"
        )
        return skipPatterns.any { path.startsWith(it) }
    }
}
