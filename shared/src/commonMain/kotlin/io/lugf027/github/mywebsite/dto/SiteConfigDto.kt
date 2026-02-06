package io.lugf027.github.mywebsite.dto

import kotlinx.serialization.Serializable

/**
 * 站点配置键名
 */
object SiteConfigKey {
    const val SITE_NAME = "site_name"
    const val SITE_DESCRIPTION = "site_description"
    const val OWNER_NAME = "owner_name"
    const val OWNER_AVATAR = "owner_avatar"
    const val OWNER_BIO = "owner_bio"
    const val OWNER_TITLE = "owner_title"
    const val GITHUB_URL = "github_url"
    const val LINKEDIN_URL = "linkedin_url"
    const val EMAIL = "email"
    const val ICP_RECORD = "icp_record"
}

/**
 * 站点配置项
 */
@Serializable
data class SiteConfigItem(
    val key: String,
    val value: String,
    val updatedAt: String
)

/**
 * 站点配置更新请求
 */
@Serializable
data class SiteConfigUpdateRequest(
    val configs: List<SiteConfigItem>
)

/**
 * 站点概览信息（首页使用）
 */
@Serializable
data class SiteOverview(
    val siteName: String,
    val siteDescription: String,
    val ownerName: String,
    val ownerAvatar: String,
    val ownerBio: String,
    val ownerTitle: String,
    val githubUrl: String,
    val linkedinUrl: String,
    val email: String,
    val icpRecord: String
)
