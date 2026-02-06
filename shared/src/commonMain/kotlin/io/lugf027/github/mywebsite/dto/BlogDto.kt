package io.lugf027.github.mywebsite.dto

import kotlinx.serialization.Serializable

/**
 * 博客状态
 */
object BlogStatus {
    const val DRAFT = "draft"
    const val PUBLISHED = "published"
}

/**
 * 博客创建/更新请求
 */
@Serializable
data class BlogRequest(
    val title: String,
    val content: String,
    val summary: String? = null,
    val status: String = BlogStatus.DRAFT
)

/**
 * 博客详情
 */
@Serializable
data class BlogDetail(
    val id: Long,
    val title: String,
    val content: String,
    val summary: String,
    val status: String,
    val authorId: Long,
    val authorName: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val publishedAt: String?
)

/**
 * 博客列表项（不含完整内容）
 */
@Serializable
data class BlogListItem(
    val id: Long,
    val title: String,
    val summary: String,
    val status: String,
    val authorName: String,
    val viewCount: Int,
    val createdAt: String,
    val publishedAt: String?
)

/**
 * 博客查询参数
 */
@Serializable
data class BlogQueryParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val keyword: String? = null,
    val status: String? = null
)
