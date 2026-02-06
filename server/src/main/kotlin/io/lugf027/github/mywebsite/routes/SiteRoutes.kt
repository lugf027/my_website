package io.lugf027.github.mywebsite.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.common.Response.success
import io.lugf027.github.mywebsite.service.SiteConfigService

/**
 * 公开站点路由
 */
fun Route.siteRoutes() {
    val siteConfigService = SiteConfigService()
    
    route(ApiRoutes.Site.BASE) {
        // 获取站点概览信息
        get("/overview") {
            val overview = siteConfigService.getOverview()
            call.success(overview)
        }
    }
}
