package io.lugf027.github.mywebsite.service

import io.lugf027.github.mywebsite.common.BlogNotFoundException
import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.dto.*
import io.lugf027.github.mywebsite.repository.BlogRepository
import io.lugf027.github.mywebsite.repository.UserRepository
import kotlin.math.ceil

/**
 * 博客服务
 */
class BlogService(
    private val blogRepository: BlogRepository = BlogRepository(),
    private val userRepository: UserRepository = UserRepository()
) : Logger() {
    
    /**
     * 获取公开博客列表（已发布）
     */
    fun getPublishedBlogs(page: Int, pageSize: Int, keyword: String? = null): PageData<BlogListItem> {
        val (blogs, total) = blogRepository.findPublished(page, pageSize, keyword)
        
        val items = blogs.map { blog ->
            val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
            blog.toListItem(authorName)
        }
        
        return PageData(
            items = items,
            total = total,
            page = page,
            pageSize = pageSize,
            totalPages = ceil(total.toDouble() / pageSize).toInt()
        )
    }
    
    /**
     * 获取博客详情（公开）
     */
    fun getBlogDetail(id: Long): BlogDetail {
        val blog = blogRepository.findById(id)
            ?: throw BlogNotFoundException()
        
        // 只有已发布的博客才能公开查看
        if (blog.status != BlogStatus.PUBLISHED) {
            throw BlogNotFoundException()
        }
        
        // 增加浏览量
        blogRepository.incrementViewCount(id)
        
        val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
        return blog.toDetail(authorName)
    }
    
    /**
     * 获取最新博客
     */
    fun getLatestBlogs(limit: Int = 5): List<BlogListItem> {
        return blogRepository.findLatest(limit).map { blog ->
            val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
            blog.toListItem(authorName)
        }
    }
    
    /**
     * 获取所有博客（后台管理）
     */
    fun getAllBlogs(page: Int, pageSize: Int, status: String? = null, keyword: String? = null): PageData<BlogListItem> {
        val (blogs, total) = blogRepository.findAll(page, pageSize, status, keyword)
        
        val items = blogs.map { blog ->
            val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
            blog.toListItem(authorName)
        }
        
        return PageData(
            items = items,
            total = total,
            page = page,
            pageSize = pageSize,
            totalPages = ceil(total.toDouble() / pageSize).toInt()
        )
    }
    
    /**
     * 获取博客详情（后台，包含草稿）
     */
    fun getBlogDetailForAdmin(id: Long): BlogDetail {
        val blog = blogRepository.findById(id)
            ?: throw BlogNotFoundException()
        
        val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
        return blog.toDetail(authorName)
    }
    
    /**
     * 创建博客
     */
    fun createBlog(request: BlogRequest, authorId: Long): BlogDetail {
        require(request.title.isNotBlank()) { "Title cannot be empty" }
        require(request.content.isNotBlank()) { "Content cannot be empty" }
        
        val summary = request.summary?.takeIf { it.isNotBlank() } 
            ?: generateSummary(request.content)
        
        val blog = blogRepository.create(
            title = request.title,
            content = request.content,
            summary = summary,
            status = request.status,
            authorId = authorId
        )
        
        val authorName = userRepository.findById(authorId)?.username ?: "Unknown"
        logger.info("Blog created: {} by user {}", request.title, authorId)
        return blog.toDetail(authorName)
    }
    
    /**
     * 更新博客
     */
    fun updateBlog(id: Long, request: BlogRequest): BlogDetail {
        require(request.title.isNotBlank()) { "Title cannot be empty" }
        require(request.content.isNotBlank()) { "Content cannot be empty" }
        
        val summary = request.summary?.takeIf { it.isNotBlank() }
            ?: generateSummary(request.content)
        
        val blog = blogRepository.update(
            id = id,
            title = request.title,
            content = request.content,
            summary = summary,
            status = request.status
        ) ?: throw BlogNotFoundException()
        
        val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
        logger.info("Blog updated: {}", id)
        return blog.toDetail(authorName)
    }
    
    /**
     * 删除博客
     */
    fun deleteBlog(id: Long) {
        if (!blogRepository.delete(id)) {
            throw BlogNotFoundException()
        }
        logger.info("Blog deleted: {}", id)
    }
    
    /**
     * 发布博客
     */
    fun publishBlog(id: Long): BlogDetail {
        val blog = blogRepository.publish(id)
            ?: throw BlogNotFoundException()
        
        val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
        logger.info("Blog published: {}", id)
        return blog.toDetail(authorName)
    }
    
    /**
     * 取消发布博客
     */
    fun unpublishBlog(id: Long): BlogDetail {
        val blog = blogRepository.unpublish(id)
            ?: throw BlogNotFoundException()
        
        val authorName = userRepository.findById(blog.authorId)?.username ?: "Unknown"
        logger.info("Blog unpublished: {}", id)
        return blog.toDetail(authorName)
    }
    
    /**
     * 生成摘要
     */
    private fun generateSummary(content: String, maxLength: Int = 200): String {
        // 移除 Markdown 标记
        val plainText = content
            .replace(Regex("#+ "), "")
            .replace(Regex("\\*\\*|__"), "")
            .replace(Regex("\\*|_"), "")
            .replace(Regex("\\[([^]]+)]\\([^)]+\\)"), "$1")
            .replace(Regex("```[\\s\\S]*?```"), "")
            .replace(Regex("`[^`]+`"), "")
            .replace(Regex("!\\[[^]]*]\\([^)]+\\)"), "")
            .replace(Regex("\\n+"), " ")
            .trim()
        
        return if (plainText.length <= maxLength) {
            plainText
        } else {
            plainText.take(maxLength - 3) + "..."
        }
    }
}
