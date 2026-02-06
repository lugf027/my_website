package io.lugf027.github.mywebsite.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.lugf027.github.mywebsite.routes.*

/**
 * 路由配置
 */
fun Application.configureRouting() {
    routing {
        // 健康检查
        get("/health") {
            call.respondText("OK", ContentType.Text.Plain, HttpStatusCode.OK)
        }
        
        // API 路由
        authRoutes()
        blogRoutes()
        siteRoutes()
        adminRoutes()
    }
}
