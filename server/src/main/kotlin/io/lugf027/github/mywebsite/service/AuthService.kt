package io.lugf027.github.mywebsite.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.lugf027.github.mywebsite.JwtConfig
import io.lugf027.github.mywebsite.common.*
import io.lugf027.github.mywebsite.config.AppConfig
import io.lugf027.github.mywebsite.dto.LoginRequest
import io.lugf027.github.mywebsite.dto.LoginResponse
import io.lugf027.github.mywebsite.dto.RegisterRequest
import io.lugf027.github.mywebsite.dto.UserInfo
import io.lugf027.github.mywebsite.repository.UserRepository
import java.security.MessageDigest
import java.util.*

/**
 * 认证服务
 */
class AuthService(
    private val userRepository: UserRepository = UserRepository()
) : Logger() {
    
    /**
     * 用户注册
     */
    fun register(request: RegisterRequest): UserInfo {
        // 验证参数
        require(request.username.length >= 3) { "Username must be at least 3 characters" }
        require(request.password.length >= 6) { "Password must be at least 6 characters" }
        require(request.email.contains("@")) { "Invalid email format" }
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.username)) {
            throw UserAlreadyExistsException("Username '${request.username}' already exists")
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("Email '${request.email}' already exists")
        }
        
        // 创建用户
        val passwordHash = hashPassword(request.password)
        val user = userRepository.create(
            username = request.username,
            passwordHash = passwordHash,
            email = request.email
        )
        
        logger.info("User registered: {}", request.username)
        return user.toUserInfo()
    }
    
    /**
     * 用户登录
     */
    fun login(request: LoginRequest): LoginResponse {
        // 查找用户
        val user = userRepository.findByUsername(request.username)
            ?: throw InvalidCredentialsException()
        
        // 验证密码
        if (!verifyPassword(request.password, user.passwordHash)) {
            throw InvalidCredentialsException()
        }
        
        // 生成 JWT Token
        val token = generateToken(user.id.value, user.username, user.role)
        
        logger.info("User logged in: {}", request.username)
        return LoginResponse(
            token = token,
            user = user.toUserInfo()
        )
    }
    
    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(userId: Long): UserInfo {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException()
        return user.toUserInfo()
    }
    
    /**
     * 生成 JWT Token
     */
    private fun generateToken(userId: Long, username: String, role: String): String {
        val expiry = Date(System.currentTimeMillis() + AppConfig.Jwt.expiryHours * 3600 * 1000)
        
        return JWT.create()
            .withIssuer(AppConfig.Jwt.issuer)
            .withAudience(AppConfig.Jwt.audience)
            .withClaim(JwtConfig.CLAIM_USER_ID, userId)
            .withClaim(JwtConfig.CLAIM_USERNAME, username)
            .withClaim(JwtConfig.CLAIM_ROLE, role)
            .withExpiresAt(expiry)
            .sign(Algorithm.HMAC256(AppConfig.Jwt.secret))
    }
    
    /**
     * 密码哈希
     */
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * 验证密码
     */
    private fun verifyPassword(password: String, hash: String): Boolean {
        return hashPassword(password) == hash
    }
}
