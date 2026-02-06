package io.lugf027.github.mywebsite.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

/**
 * CORS 配置
 */
fun Application.configureCors() {
    install(CORS) {
        // 允许的请求方法
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Options)
        
        // 允许的请求头
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.Origin)
        allowHeader(HttpHeaders.AccessControlRequestHeaders)
        allowHeader(HttpHeaders.AccessControlRequestMethod)
        
        // 允许凭证
        allowCredentials = true
        
        // 允许非简单内容类型
        allowNonSimpleContentTypes = true
        
        // 开发环境允许所有来源
        anyHost()
    }
}
