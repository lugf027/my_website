package io.lugf027.github.mywebsite.model.entities

import io.lugf027.github.mywebsite.dto.BlogDetail
import io.lugf027.github.mywebsite.dto.BlogListItem
import io.lugf027.github.mywebsite.model.tables.Blogs
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.format.DateTimeFormatter

/**
 * 博客实体
 */
class Blog(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Blog>(Blogs)
    
    var title by Blogs.title
    var content by Blogs.content
    var summary by Blogs.summary
    var status by Blogs.status
    var authorId by Blogs.authorId
    var viewCount by Blogs.viewCount
    var createdAt by Blogs.createdAt
    var updatedAt by Blogs.updatedAt
    var publishedAt by Blogs.publishedAt
    
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    /**
     * 转换为详情 DTO
     */
    fun toDetail(authorName: String): BlogDetail {
        return BlogDetail(
            id = id.value,
            title = title,
            content = content,
            summary = summary,
            status = status,
            authorId = authorId,
            authorName = authorName,
            viewCount = viewCount,
            createdAt = createdAt.format(formatter),
            updatedAt = updatedAt.format(formatter),
            publishedAt = publishedAt?.format(formatter)
        )
    }
    
    /**
     * 转换为列表项 DTO
     */
    fun toListItem(authorName: String): BlogListItem {
        return BlogListItem(
            id = id.value,
            title = title,
            summary = summary,
            status = status,
            authorName = authorName,
            viewCount = viewCount,
            createdAt = createdAt.format(formatter),
            publishedAt = publishedAt?.format(formatter)
        )
    }
}
