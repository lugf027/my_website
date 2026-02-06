package io.lugf027.github.mywebsite.dto

import kotlinx.serialization.Serializable

/**
 * 统计概览（仪表盘）
 */
@Serializable
data class StatisticsOverview(
    val totalPv: Long,
    val todayPv: Long,
    val totalUv: Long,
    val todayUv: Long,
    val totalBlogs: Int,
    val publishedBlogs: Int,
    val totalUsers: Int
)

/**
 * 每日统计数据
 */
@Serializable
data class DailyStatistics(
    val date: String,
    val pv: Long,
    val uv: Long
)

/**
 * 热门博客统计
 */
@Serializable
data class PopularBlog(
    val id: Long,
    val title: String,
    val viewCount: Int
)

/**
 * 访问来源统计
 */
@Serializable
data class ReferrerStatistics(
    val referrer: String,
    val count: Long
)

/**
 * 设备类型统计
 */
@Serializable
data class DeviceStatistics(
    val device: String,
    val count: Long
)

/**
 * 完整统计报表
 */
@Serializable
data class StatisticsReport(
    val overview: StatisticsOverview,
    val dailyStats: List<DailyStatistics>,
    val popularBlogs: List<PopularBlog>,
    val referrers: List<ReferrerStatistics>,
    val devices: List<DeviceStatistics>
)

/**
 * 统计查询参数
 */
@Serializable
data class StatisticsQueryParams(
    val startDate: String? = null,
    val endDate: String? = null
)
