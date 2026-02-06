package io.lugf027.github.mywebsite.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.lugf027.github.mywebsite.PaginationConfig
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.common.Response.success
import io.lugf027.github.mywebsite.service.BlogService

/**
 * 公开博客路由
 */
fun Route.blogRoutes() {
    val blogService = BlogService()
    
    route(ApiRoutes.Blog.BASE) {
        // 获取已发布博客列表
        get {
            val page = call.parameters["page"]?.toIntOrNull() ?: PaginationConfig.DEFAULT_PAGE
            val pageSize = call.parameters["pageSize"]?.toIntOrNull() ?: PaginationConfig.DEFAULT_PAGE_SIZE
            val keyword = call.parameters["keyword"]
            
            val result = blogService.getPublishedBlogs(page, pageSize, keyword)
            call.success(result)
        }
        
        // 获取最新博客
        get("/latest") {
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 5
            val blogs = blogService.getLatestBlogs(limit)
            call.success(blogs)
        }
        
        // 获取博客详情
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid blog ID")
            val blog = blogService.getBlogDetail(id)
            call.success(blog)
        }
    }
}
