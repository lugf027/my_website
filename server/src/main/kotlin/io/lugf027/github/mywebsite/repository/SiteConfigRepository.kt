package io.lugf027.github.mywebsite.repository

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.SiteConfigKey
import io.lugf027.github.mywebsite.dto.SiteOverview
import io.lugf027.github.mywebsite.model.entities.SiteConfig
import io.lugf027.github.mywebsite.model.tables.SiteConfigs
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

/**
 * 站点配置数据访问层
 */
class SiteConfigRepository : Logger() {
    
    /**
     * 获取配置值
     */
    fun getValue(key: String): String? = transaction {
        SiteConfig.find { SiteConfigs.key eq key }.firstOrNull()?.value
    }
    
    /**
     * 设置配置值
     */
    fun setValue(key: String, value: String): SiteConfig = transaction {
        val existing = SiteConfig.find { SiteConfigs.key eq key }.firstOrNull()
        if (existing != null) {
            existing.value = value
            existing.updatedAt = LocalDateTime.now()
            existing
        } else {
            SiteConfig.new {
                this.key = key
                this.value = value
                this.updatedAt = LocalDateTime.now()
            }
        }.also {
            logger.debug("Config updated: {} = {}", key, value)
        }
    }
    
    /**
     * 批量更新配置
     */
    fun batchUpdate(configs: Map<String, String>): Unit = transaction {
        configs.forEach { (key, value) ->
            setValue(key, value)
        }
    }
    
    /**
     * 获取所有配置
     */
    fun findAll(): List<SiteConfig> = transaction {
        SiteConfig.all().toList()
    }
    
    /**
     * 获取站点概览信息
     */
    fun getOverview(): SiteOverview = transaction {
        val configs = SiteConfig.all().associateBy({ it.key }, { it.value })
        SiteOverview(
            siteName = configs[SiteConfigKey.SITE_NAME] ?: "My Website",
            siteDescription = configs[SiteConfigKey.SITE_DESCRIPTION] ?: "A personal website",
            ownerName = configs[SiteConfigKey.OWNER_NAME] ?: "Anonymous",
            ownerAvatar = configs[SiteConfigKey.OWNER_AVATAR] ?: "",
            ownerBio = configs[SiteConfigKey.OWNER_BIO] ?: "",
            ownerTitle = configs[SiteConfigKey.OWNER_TITLE] ?: "",
            githubUrl = configs[SiteConfigKey.GITHUB_URL] ?: "",
            linkedinUrl = configs[SiteConfigKey.LINKEDIN_URL] ?: "",
            email = configs[SiteConfigKey.EMAIL] ?: "",
            icpRecord = configs[SiteConfigKey.ICP_RECORD] ?: ""
        )
    }
    
    /**
     * 初始化默认配置
     */
    fun initDefaults(): Unit = transaction {
        val defaults = mapOf(
            SiteConfigKey.SITE_NAME to "My Website",
            SiteConfigKey.SITE_DESCRIPTION to "Welcome to my personal website",
            SiteConfigKey.OWNER_NAME to "Your Name",
            SiteConfigKey.OWNER_AVATAR to "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
            SiteConfigKey.OWNER_BIO to "Hello, I'm a developer.",
            SiteConfigKey.OWNER_TITLE to "Full Stack Developer",
            SiteConfigKey.GITHUB_URL to "https://github.com",
            SiteConfigKey.LINKEDIN_URL to "",
            SiteConfigKey.EMAIL to "contact@example.com",
            SiteConfigKey.ICP_RECORD to ""
        )
        
        defaults.forEach { (key, value) ->
            if (getValue(key) == null) {
                setValue(key, value)
            }
        }
        logger.info("Default site configs initialized")
    }
}
