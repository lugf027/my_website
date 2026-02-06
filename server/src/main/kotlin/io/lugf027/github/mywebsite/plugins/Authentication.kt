package io.lugf027.github.mywebsite.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.lugf027.github.mywebsite.JwtConfig
import io.lugf027.github.mywebsite.config.AppConfig

/**
 * JWT 认证配置
 */
fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = AppConfig.Jwt.realm
            
            verifier(
                JWT.require(Algorithm.HMAC256(AppConfig.Jwt.secret))
                    .withIssuer(AppConfig.Jwt.issuer)
                    .withAudience(AppConfig.Jwt.audience)
                    .build()
            )
            
            validate { credential ->
                val userId = credential.payload.getClaim(JwtConfig.CLAIM_USER_ID).asLong()
                val username = credential.payload.getClaim(JwtConfig.CLAIM_USERNAME).asString()
                val role = credential.payload.getClaim(JwtConfig.CLAIM_ROLE).asString()
                
                if (userId != null && username != null) {
                    UserPrincipal(userId, username, role ?: "user")
                } else {
                    null
                }
            }
        }
    }
}

/**
 * 用户主体信息
 */
data class UserPrincipal(
    val userId: Long,
    val username: String,
    val role: String
) : Principal

/**
 * 获取当前用户主体
 */
fun ApplicationCall.userPrincipal(): UserPrincipal? {
    return principal<UserPrincipal>()
}

/**
 * 获取当前用户 ID
 */
fun ApplicationCall.userId(): Long? {
    return userPrincipal()?.userId
}
