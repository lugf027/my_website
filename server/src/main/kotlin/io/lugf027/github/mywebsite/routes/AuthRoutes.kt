package io.lugf027.github.mywebsite.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.common.Response.success
import io.lugf027.github.mywebsite.dto.LoginRequest
import io.lugf027.github.mywebsite.dto.RegisterRequest
import io.lugf027.github.mywebsite.service.AuthService
import io.ktor.server.auth.*
import io.lugf027.github.mywebsite.plugins.userId

/**
 * 认证路由
 */
fun Route.authRoutes() {
    val authService = AuthService()
    
    route(ApiRoutes.Auth.BASE) {
        // 用户注册
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val user = authService.register(request)
            call.success(user, "Registration successful")
        }
        
        // 用户登录
        post("/login") {
            val request = call.receive<LoginRequest>()
            val response = authService.login(request)
            call.success(response, "Login successful")
        }
        
        // 获取当前用户信息（需要认证）
        authenticate("auth-jwt") {
            get("/me") {
                val userId = call.userId() 
                    ?: throw IllegalStateException("User not authenticated")
                val user = authService.getCurrentUser(userId)
                call.success(user)
            }
        }
    }
}
