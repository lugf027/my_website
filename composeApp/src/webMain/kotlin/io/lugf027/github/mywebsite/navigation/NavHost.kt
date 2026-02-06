package io.lugf027.github.mywebsite.navigation

import androidx.compose.runtime.*
import kotlinx.browser.window

/**
 * 路由定义
 */
sealed class Screen(val route: String) {
    data object Home : Screen("/")
    data object BlogList : Screen("/blogs")
    data class BlogDetail(val id: Long) : Screen("/blogs/$id")
    data object Login : Screen("/admin/login")
    data object Register : Screen("/admin/register")
    data object Dashboard : Screen("/admin/dashboard")
    data object BlogManage : Screen("/admin/blogs")
    data class BlogEditor(val id: Long? = null) : Screen(if (id != null) "/admin/blogs/edit/$id" else "/admin/blogs/new")
    data object SiteConfig : Screen("/admin/site-config")
    data object Statistics : Screen("/admin/statistics")
}

/**
 * 导航控制器
 */
class NavController {
    var currentRoute by mutableStateOf(window.location.pathname)
        private set
    
    init {
        // 监听浏览器历史变化
        window.onpopstate = {
            currentRoute = window.location.pathname
        }
    }
    
    /**
     * 导航到指定路由
     */
    fun navigate(route: String) {
        window.history.pushState(null, "", route)
        currentRoute = route
    }
    
    /**
     * 导航到 Screen
     */
    fun navigate(screen: Screen) {
        navigate(screen.route)
    }
    
    /**
     * 返回上一页
     */
    fun goBack() {
        window.history.back()
    }
    
    /**
     * 替换当前路由（不添加历史记录）
     */
    fun replace(route: String) {
        window.history.replaceState(null, "", route)
        currentRoute = route
    }
}

/**
 * 创建并记住 NavController
 */
@Composable
fun rememberNavController(): NavController {
    return remember { NavController() }
}

/**
 * 路由匹配结果
 */
data class RouteMatch(
    val matched: Boolean,
    val params: Map<String, String> = emptyMap()
)

/**
 * 匹配路由
 */
fun matchRoute(pattern: String, path: String): RouteMatch {
    val patternParts = pattern.split("/").filter { it.isNotEmpty() }
    val pathParts = path.split("/").filter { it.isNotEmpty() }
    
    if (patternParts.size != pathParts.size) {
        return RouteMatch(false)
    }
    
    val params = mutableMapOf<String, String>()
    
    for (i in patternParts.indices) {
        val patternPart = patternParts[i]
        val pathPart = pathParts[i]
        
        when {
            patternPart.startsWith("{") && patternPart.endsWith("}") -> {
                val paramName = patternPart.drop(1).dropLast(1)
                params[paramName] = pathPart
            }
            patternPart != pathPart -> return RouteMatch(false)
        }
    }
    
    return RouteMatch(true, params)
}
