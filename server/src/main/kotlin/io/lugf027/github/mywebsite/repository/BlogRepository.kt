package io.lugf027.github.mywebsite.repository

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.BlogStatus
import io.lugf027.github.mywebsite.model.entities.Blog
import io.lugf027.github.mywebsite.model.tables.Blogs
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

/**
 * 博客数据访问层
 */
class BlogRepository : Logger() {
    
    /**
     * 根据 ID 查找博客
     */
    fun findById(id: Long): Blog? = transaction {
        Blog.findById(id)
    }
    
    /**
     * 分页查询博客（公开列表，只查已发布的）
     */
    fun findPublished(page: Int, pageSize: Int, keyword: String? = null): Pair<List<Blog>, Long> = transaction {
        val query = if (keyword.isNullOrBlank()) {
            Blog.find { Blogs.status eq BlogStatus.PUBLISHED }
        } else {
            Blog.find { 
                (Blogs.status eq BlogStatus.PUBLISHED) and 
                ((Blogs.title like "%$keyword%") or (Blogs.summary like "%$keyword%"))
            }
        }
        
        val total = query.count()
        val items = query
            .orderBy(Blogs.publishedAt to SortOrder.DESC)
            .limit(pageSize, ((page - 1) * pageSize).toLong())
            .toList()
        
        items to total
    }
    
    /**
     * 分页查询所有博客（后台管理）
     */
    fun findAll(page: Int, pageSize: Int, status: String? = null, keyword: String? = null): Pair<List<Blog>, Long> = transaction {
        val baseCondition: Op<Boolean>? = when {
            status != null && keyword != null -> 
                (Blogs.status eq status) and ((Blogs.title like "%$keyword%") or (Blogs.summary like "%$keyword%"))
            status != null -> Blogs.status eq status
            keyword != null -> (Blogs.title like "%$keyword%") or (Blogs.summary like "%$keyword%")
            else -> null
        }
        
        val query = if (baseCondition != null) {
            Blog.find { baseCondition }
        } else {
            Blog.all()
        }
        
        val total = query.count()
        val items = query
            .orderBy(Blogs.createdAt to SortOrder.DESC)
            .limit(pageSize, ((page - 1) * pageSize).toLong())
            .toList()
        
        items to total
    }
    
    /**
     * 获取最新博客
     */
    fun findLatest(limit: Int = 5): List<Blog> = transaction {
        Blog.find { Blogs.status eq BlogStatus.PUBLISHED }
            .orderBy(Blogs.publishedAt to SortOrder.DESC)
            .limit(limit)
            .toList()
    }
    
    /**
     * 创建博客
     */
    fun create(title: String, content: String, summary: String, status: String, authorId: Long): Blog = transaction {
        val now = LocalDateTime.now()
        Blog.new {
            this.title = title
            this.content = content
            this.summary = summary
            this.status = status
            this.authorId = authorId
            this.viewCount = 0
            this.createdAt = now
            this.updatedAt = now
            if (status == BlogStatus.PUBLISHED) {
                this.publishedAt = now
            }
        }.also {
            logger.info("Blog created: {}", title)
        }
    }
    
    /**
     * 更新博客
     */
    fun update(id: Long, title: String, content: String, summary: String, status: String): Blog? = transaction {
        Blog.findById(id)?.apply {
            this.title = title
            this.content = content
            this.summary = summary
            val wasPublished = this.status == BlogStatus.PUBLISHED
            this.status = status
            this.updatedAt = LocalDateTime.now()
            // 如果从草稿变为发布，设置发布时间
            if (!wasPublished && status == BlogStatus.PUBLISHED) {
                this.publishedAt = LocalDateTime.now()
            }
        }
    }
    
    /**
     * 删除博客
     */
    fun delete(id: Long): Boolean = transaction {
        Blog.findById(id)?.let {
            it.delete()
            logger.info("Blog deleted: {}", id)
            true
        } ?: false
    }
    
    /**
     * 发布博客
     */
    fun publish(id: Long): Blog? = transaction {
        Blog.findById(id)?.apply {
            if (this.status != BlogStatus.PUBLISHED) {
                this.status = BlogStatus.PUBLISHED
                this.publishedAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
                logger.info("Blog published: {}", id)
            }
        }
    }
    
    /**
     * 取消发布博客
     */
    fun unpublish(id: Long): Blog? = transaction {
        Blog.findById(id)?.apply {
            this.status = BlogStatus.DRAFT
            this.updatedAt = LocalDateTime.now()
            logger.info("Blog unpublished: {}", id)
        }
    }
    
    /**
     * 增加浏览量
     */
    fun incrementViewCount(id: Long): Unit = transaction {
        Blog.findById(id)?.let {
            it.viewCount += 1
        }
    }
    
    /**
     * 统计博客数量
     */
    fun count(status: String? = null): Long = transaction {
        if (status != null) {
            Blog.find { Blogs.status eq status }.count()
        } else {
            Blog.count()
        }
    }
    
    /**
     * 获取热门博客
     */
    fun findPopular(limit: Int = 10): List<Blog> = transaction {
        Blog.find { Blogs.status eq BlogStatus.PUBLISHED }
            .orderBy(Blogs.viewCount to SortOrder.DESC)
            .limit(limit)
            .toList()
    }
}
