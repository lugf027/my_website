package io.lugf027.github.mywebsite.api

/**
 * API 路径定义
 */
object ApiRoutes {
    const val BASE_URL = "/api/v1"
    
    // 认证相关
    object Auth {
        const val BASE = "$BASE_URL/auth"
        const val REGISTER = "$BASE/register"
        const val LOGIN = "$BASE/login"
        const val ME = "$BASE/me"
    }
    
    // 博客相关（公开）
    object Blog {
        const val BASE = "$BASE_URL/blogs"
        const val LIST = BASE
        const val DETAIL = "$BASE/{id}"
        const val LATEST = "$BASE/latest"
        
        fun detail(id: Long) = "$BASE/$id"
    }
    
    // 后台管理
    object Admin {
        const val BASE = "$BASE_URL/admin"
        
        // 博客管理
        object Blogs {
            const val BASE = "${Admin.BASE}/blogs"
            const val LIST = BASE
            const val CREATE = BASE
            const val UPDATE = "$BASE/{id}"
            const val DELETE = "$BASE/{id}"
            const val PUBLISH = "$BASE/{id}/publish"
            const val UNPUBLISH = "$BASE/{id}/unpublish"
            
            fun update(id: Long) = "$BASE/$id"
            fun delete(id: Long) = "$BASE/$id"
            fun publish(id: Long) = "$BASE/$id/publish"
            fun unpublish(id: Long) = "$BASE/$id/unpublish"
        }
        
        // 站点配置
        object SiteConfig {
            const val BASE = "${Admin.BASE}/site-config"
            const val GET = BASE
            const val UPDATE = BASE
        }
        
        // 统计数据
        object Statistics {
            const val BASE = "${Admin.BASE}/statistics"
            const val OVERVIEW = "$BASE/overview"
            const val REPORT = "$BASE/report"
        }
    }
    
    // 公开站点信息
    object Site {
        const val BASE = "$BASE_URL/site"
        const val OVERVIEW = "$BASE/overview"
    }
}
