package io.lugf027.github.mywebsite.model.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 站点配置表
 */
object SiteConfigs : LongIdTable("site_configs") {
    val key = varchar("config_key", 100).uniqueIndex()
    val value = text("config_value")
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
