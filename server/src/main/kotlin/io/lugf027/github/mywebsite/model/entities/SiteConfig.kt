package io.lugf027.github.mywebsite.model.entities

import io.lugf027.github.mywebsite.dto.SiteConfigItem
import io.lugf027.github.mywebsite.model.tables.SiteConfigs
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.format.DateTimeFormatter

/**
 * 站点配置实体
 */
class SiteConfig(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SiteConfig>(SiteConfigs)
    
    var key by SiteConfigs.key
    var value by SiteConfigs.value
    var updatedAt by SiteConfigs.updatedAt
    
    /**
     * 转换为 DTO
     */
    fun toDto(): SiteConfigItem {
        return SiteConfigItem(
            key = key,
            value = value,
            updatedAt = updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}
