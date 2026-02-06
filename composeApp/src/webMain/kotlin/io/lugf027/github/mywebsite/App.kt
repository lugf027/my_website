package io.lugf027.github.mywebsite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.lugf027.github.mywebsite.navigation.matchRoute
import io.lugf027.github.mywebsite.navigation.rememberNavController
import io.lugf027.github.mywebsite.ui.admin.*
import io.lugf027.github.mywebsite.ui.blog.BlogDetailPage
import io.lugf027.github.mywebsite.ui.blog.BlogListPage
import io.lugf027.github.mywebsite.ui.home.HomePage
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.ui.theme.AppTheme
import io.lugf027.github.mywebsite.utils.Storage

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColors.Background
        ) {
            // Router
            val currentRoute = navController.currentRoute
            
            when {
                // Home
                currentRoute == "/" || currentRoute.isEmpty() -> {
                    HomePage(navController = navController)
                }
                
                // Blog List
                currentRoute == "/blogs" -> {
                    BlogListPage(navController = navController)
                }
                
                // Blog Detail
                matchRoute("/blogs/{id}", currentRoute).let { match ->
                    if (match.matched) {
                        val blogId = match.params["id"]?.toLongOrNull()
                        if (blogId != null) {
                            BlogDetailPage(
                                navController = navController,
                                blogId = blogId
                            )
                            return@Surface
                        }
                    }
                    false
                } -> { }
                
                // Admin Login
                currentRoute == "/admin/login" -> {
                    LoginPage(navController = navController)
                }
                
                // Admin Register
                currentRoute == "/admin/register" -> {
                    RegisterPage(navController = navController)
                }
                
                // Admin Dashboard (protected)
                currentRoute == "/admin/dashboard" -> {
                    if (Storage.isLoggedIn()) {
                        DashboardPage(navController = navController)
                    } else {
                        navController.navigate("/admin/login")
                    }
                }
                
                // Admin Blog Manage (protected)
                currentRoute == "/admin/blogs" -> {
                    if (Storage.isLoggedIn()) {
                        BlogManagePage(navController = navController)
                    } else {
                        navController.navigate("/admin/login")
                    }
                }
                
                // Admin Blog Editor - New (protected)
                currentRoute == "/admin/blogs/new" -> {
                    if (Storage.isLoggedIn()) {
                        BlogEditorPage(
                            navController = navController,
                            blogId = null
                        )
                    } else {
                        navController.navigate("/admin/login")
                    }
                }
                
                // Admin Blog Editor - Edit (protected)
                matchRoute("/admin/blogs/edit/{id}", currentRoute).let { match ->
                    if (match.matched) {
                        val blogId = match.params["id"]?.toLongOrNull()
                        if (blogId != null && Storage.isLoggedIn()) {
                            BlogEditorPage(
                                navController = navController,
                                blogId = blogId
                            )
                            return@Surface
                        } else if (!Storage.isLoggedIn()) {
                            navController.navigate("/admin/login")
                            return@Surface
                        }
                    }
                    false
                } -> { }
                
                // Admin Site Config (protected)
                currentRoute == "/admin/site-config" -> {
                    if (Storage.isLoggedIn()) {
                        SiteConfigPage(navController = navController)
                    } else {
                        navController.navigate("/admin/login")
                    }
                }
                
                // Admin Statistics (protected)
                currentRoute == "/admin/statistics" -> {
                    if (Storage.isLoggedIn()) {
                        StatisticsPage(navController = navController)
                    } else {
                        navController.navigate("/admin/login")
                    }
                }
                
                // Default: Home
                else -> {
                    HomePage(navController = navController)
                }
            }
        }
    }
}