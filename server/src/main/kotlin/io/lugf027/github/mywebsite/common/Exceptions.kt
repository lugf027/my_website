package io.lugf027.github.mywebsite.common

/**
 * 自定义业务异常基类
 */
open class AppException(
    val errorCode: Int,
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 错误码定义
 */
object ErrorCode {
    // 通用错误 1xxx
    const val UNKNOWN_ERROR = 1000
    const val INVALID_PARAMETER = 1001
    const val NOT_FOUND = 1002
    const val UNAUTHORIZED = 1003
    const val FORBIDDEN = 1004
    
    // 用户相关 2xxx
    const val USER_NOT_FOUND = 2001
    const val USER_ALREADY_EXISTS = 2002
    const val INVALID_CREDENTIALS = 2003
    const val INVALID_TOKEN = 2004
    
    // 博客相关 3xxx
    const val BLOG_NOT_FOUND = 3001
    const val BLOG_ALREADY_EXISTS = 3002
    
    // 数据库相关 4xxx
    const val DATABASE_ERROR = 4001
}

/**
 * 参数无效异常
 */
class InvalidParameterException(
    message: String = "Invalid parameter"
) : AppException(ErrorCode.INVALID_PARAMETER, message)

/**
 * 资源未找到异常
 */
class NotFoundException(
    message: String = "Resource not found"
) : AppException(ErrorCode.NOT_FOUND, message)

/**
 * 未授权异常
 */
class UnauthorizedException(
    message: String = "Unauthorized"
) : AppException(ErrorCode.UNAUTHORIZED, message)

/**
 * 禁止访问异常
 */
class ForbiddenException(
    message: String = "Forbidden"
) : AppException(ErrorCode.FORBIDDEN, message)

/**
 * 用户不存在异常
 */
class UserNotFoundException(
    message: String = "User not found"
) : AppException(ErrorCode.USER_NOT_FOUND, message)

/**
 * 用户已存在异常
 */
class UserAlreadyExistsException(
    message: String = "User already exists"
) : AppException(ErrorCode.USER_ALREADY_EXISTS, message)

/**
 * 凭证无效异常
 */
class InvalidCredentialsException(
    message: String = "Invalid username or password"
) : AppException(ErrorCode.INVALID_CREDENTIALS, message)

/**
 * 博客不存在异常
 */
class BlogNotFoundException(
    message: String = "Blog not found"
) : AppException(ErrorCode.BLOG_NOT_FOUND, message)

/**
 * 数据库异常
 */
class DatabaseException(
    message: String = "Database error",
    cause: Throwable? = null
) : AppException(ErrorCode.DATABASE_ERROR, message, cause)
