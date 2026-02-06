package io.lugf027.github.mywebsite.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.lugf027.github.mywebsite.PaginationConfig
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.common.Response.created
import io.lugf027.github.mywebsite.common.Response.success
import io.lugf027.github.mywebsite.dto.BlogRequest
import io.lugf027.github.mywebsite.dto.SiteConfigUpdateRequest
import io.lugf027.github.mywebsite.plugins.userId
import io.lugf027.github.mywebsite.service.BlogService
import io.lugf027.github.mywebsite.service.SiteConfigService
import io.lugf027.github.mywebsite.service.StatisticsService

/**
 * 后台管理路由（需要认证）
 */
fun Route.adminRoutes() {
    val blogService = BlogService()
    val siteConfigService = SiteConfigService()
    val statisticsService = StatisticsService()
    
    authenticate("auth-jwt") {
        route(ApiRoutes.Admin.BASE) {
            // 博客管理
            route("/blogs") {
                // 获取所有博客（包括草稿）
                get {
                    val page = call.parameters["page"]?.toIntOrNull() ?: PaginationConfig.DEFAULT_PAGE
                    val pageSize = call.parameters["pageSize"]?.toIntOrNull() ?: PaginationConfig.DEFAULT_PAGE_SIZE
                    val status = call.parameters["status"]
                    val keyword = call.parameters["keyword"]
                    
                    val result = blogService.getAllBlogs(page, pageSize, status, keyword)
                    call.success(result)
                }
                
                // 获取博客详情（包括草稿）
                get("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid blog ID")
                    val blog = blogService.getBlogDetailForAdmin(id)
                    call.success(blog)
                }
                
                // 创建博客
                post {
                    val userId = call.userId()
                        ?: throw IllegalStateException("User not authenticated")
                    val request = call.receive<BlogRequest>()
                    val blog = blogService.createBlog(request, userId)
                    call.created(blog, "Blog created successfully")
                }
                
                // 更新博客
                put("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid blog ID")
                    val request = call.receive<BlogRequest>()
                    val blog = blogService.updateBlog(id, request)
                    call.success(blog, "Blog updated successfully")
                }
                
                // 删除博客
                delete("/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid blog ID")
                    blogService.deleteBlog(id)
                    call.success<Unit>(message = "Blog deleted successfully")
                }
                
                // 发布博客
                post("/{id}/publish") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid blog ID")
                    val blog = blogService.publishBlog(id)
                    call.success(blog, "Blog published successfully")
                }
                
                // 取消发布博客
                post("/{id}/unpublish") {
                    val id = call.parameters["id"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid blog ID")
                    val blog = blogService.unpublishBlog(id)
                    call.success(blog, "Blog unpublished successfully")
                }
            }
            
            // 站点配置
            route("/site-config") {
                // 获取所有配置
                get {
                    val configs = siteConfigService.getAllConfigs()
                    call.success(configs)
                }
                
                // 更新配置
                put {
                    val request = call.receive<SiteConfigUpdateRequest>()
                    siteConfigService.updateConfigs(request.configs)
                    call.success<Unit>(message = "Site config updated successfully")
                }
            }
            
            // 统计数据
            route("/statistics") {
                // 获取概览
                get("/overview") {
                    val overview = statisticsService.getOverview()
                    call.success(overview)
                }
                
                // 获取详细报表
                get("/report") {
                    val startDate = call.parameters["startDate"]
                    val endDate = call.parameters["endDate"]
                    val report = statisticsService.getReport(startDate, endDate)
                    call.success(report)
                }
            }
        }
    }
}
