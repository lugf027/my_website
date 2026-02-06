package io.lugf027.github.mywebsite.model.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 访问日志表
 */
object AccessLogs : LongIdTable("access_logs") {
    val path = varchar("path", 500)
    val method = varchar("method", 10)
    val ip = varchar("ip", 50)
    val userAgent = varchar("user_agent", 500).nullable()
    val referer = varchar("referer", 500).nullable()
    val userId = long("user_id").nullable()
    val statusCode = integer("status_code").default(200)
    val duration = long("duration").default(0)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    
    init {
        index(false, createdAt)
        index(false, path)
        index(false, ip)
    }
}
