package io.lugf027.github.mywebsite

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.plugins.*
import io.lugf027.github.mywebsite.service.SiteConfigService

fun main() {
    embeddedServer(
        Netty,
        port = ServerConfig.DEFAULT_PORT,
        host = ServerConfig.DEFAULT_HOST,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    val logger = object : Logger() {}.logger
    
    logger.info("Starting MyWebsite Server...")
    
    // 配置插件（顺序很重要）
    configureSerialization()
    configureAuthentication()
    configureCors()
    configureStatusPages()
    configureLogging()
    configureDatabase()
    configureAccessLogging()
    configureRouting()
    
    // 初始化默认站点配置
    SiteConfigService().initDefaults()
    
    logger.info("MyWebsite Server started on port {}", ServerConfig.DEFAULT_PORT)
}