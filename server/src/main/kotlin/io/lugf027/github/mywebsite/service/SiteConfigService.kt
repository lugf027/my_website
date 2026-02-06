package io.lugf027.github.mywebsite.service

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.SiteConfigItem
import io.lugf027.github.mywebsite.dto.SiteOverview
import io.lugf027.github.mywebsite.repository.SiteConfigRepository

/**
 * 站点配置服务
 */
class SiteConfigService(
    private val siteConfigRepository: SiteConfigRepository = SiteConfigRepository()
) : Logger() {
    
    /**
     * 获取站点概览（公开）
     */
    fun getOverview(): SiteOverview {
        return siteConfigRepository.getOverview()
    }
    
    /**
     * 获取所有配置（后台）
     */
    fun getAllConfigs(): List<SiteConfigItem> {
        return siteConfigRepository.findAll().map { it.toDto() }
    }
    
    /**
     * 更新配置
     */
    fun updateConfigs(configs: List<SiteConfigItem>) {
        val configMap = configs.associate { it.key to it.value }
        siteConfigRepository.batchUpdate(configMap)
        logger.info("Site configs updated: {} items", configs.size)
    }
    
    /**
     * 初始化默认配置
     */
    fun initDefaults() {
        siteConfigRepository.initDefaults()
    }
}
