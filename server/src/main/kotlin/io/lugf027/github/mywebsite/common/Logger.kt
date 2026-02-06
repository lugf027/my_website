package io.lugf027.github.mywebsite.common

import org.slf4j.LoggerFactory

/**
 * 日志工具封装
 * 
 * 使用方式:
 * ```
 * class MyService {
 *     companion object : Logger()
 * }
 * 
 * // 然后在类中使用
 * logger.info("message")
 * logger.debug("debug message with {}", param)
 * logger.error("error message", exception)
 * ```
 */
open class Logger {
    val logger: org.slf4j.Logger by lazy { 
        LoggerFactory.getLogger(this::class.java.enclosingClass ?: this::class.java) 
    }
}

/**
 * 为任意类创建 logger 的扩展函数
 */
inline fun <reified T> T.logger(): org.slf4j.Logger = LoggerFactory.getLogger(T::class.java)

/**
 * 访问日志记录器（用于记录请求访问）
 */
object AccessLogger {
    private val logger = LoggerFactory.getLogger("io.lugf027.github.mywebsite.ACCESS")
    
    fun log(
        method: String,
        path: String,
        status: Int,
        duration: Long,
        ip: String,
        userAgent: String?,
        userId: Long? = null
    ) {
        logger.info(
            "{} {} {} {}ms ip={} ua={} userId={}",
            method, path, status, duration, ip, userAgent ?: "-", userId ?: "-"
        )
    }
}
