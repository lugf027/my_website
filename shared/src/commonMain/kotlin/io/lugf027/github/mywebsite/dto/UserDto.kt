package io.lugf027.github.mywebsite.dto

import kotlinx.serialization.Serializable

/**
 * 用户注册请求
 */
@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)

/**
 * 用户登录请求
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * 登录响应数据
 */
@Serializable
data class LoginResponse(
    val token: String,
    val user: UserInfo
)

/**
 * 用户信息（不含敏感数据）
 */
@Serializable
data class UserInfo(
    val id: Long,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String
)

/**
 * 用户角色枚举
 */
object UserRole {
    const val ADMIN = "admin"
    const val USER = "user"
}
