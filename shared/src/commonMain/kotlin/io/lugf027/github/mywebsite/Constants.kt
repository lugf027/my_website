package io.lugf027.github.mywebsite

/**
 * 服务端配置常量
 */
object ServerConfig {
    const val DEFAULT_PORT = 8080
    const val DEFAULT_HOST = "0.0.0.0"
}

/**
 * 分页默认配置
 */
object PaginationConfig {
    const val DEFAULT_PAGE = 1
    const val DEFAULT_PAGE_SIZE = 10
    const val MAX_PAGE_SIZE = 100
}

/**
 * JWT 配置
 */
object JwtConfig {
    const val DEFAULT_EXPIRY_HOURS = 24L
    const val CLAIM_USER_ID = "userId"
    const val CLAIM_USERNAME = "username"
    const val CLAIM_ROLE = "role"
}

// 保持向后兼容
const val SERVER_PORT = ServerConfig.DEFAULT_PORT