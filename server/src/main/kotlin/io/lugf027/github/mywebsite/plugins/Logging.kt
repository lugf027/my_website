package io.lugf027.github.mywebsite.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.origin
import io.ktor.server.request.*
import io.lugf027.github.mywebsite.service.StatisticsService
import org.slf4j.event.Level

/**
 * 请求日志配置
 */
fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/api") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val duration = call.processingTimeMillis()
            "$httpMethod $path -> $status (${duration}ms)"
        }
    }
}

/**
 * 访问日志记录中间件
 */
fun Application.configureAccessLogging() {
    val statisticsService = StatisticsService()
    
    intercept(ApplicationCallPipeline.Monitoring) {
        val startTime = System.currentTimeMillis()
        
        proceed()
        
        val duration = System.currentTimeMillis() - startTime
        val request = call.request
        
        // 异步记录访问日志
        statisticsService.logAccess(
            path = request.path(),
            method = request.httpMethod.value,
            ip = request.origin.remoteHost,
            userAgent = request.userAgent(),
            referer = request.header("Referer"),
            userId = call.userId(),
            statusCode = call.response.status()?.value ?: 200,
            duration = duration
        )
    }
}
