package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.utils.Storage

/**
 * 后台管理侧边栏
 */
@Composable
fun AdminSidebar(
    navController: NavController,
    currentRoute: String
) {
    Column(
        modifier = Modifier
            .width(240.dp)
            .fillMaxHeight()
            .background(AppColors.Surface)
            .padding(16.dp)
    ) {
        // Logo
        Text(
            text = "📊 后台管理",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Menu Items
        SidebarItem(
            icon = "🏠",
            label = "仪表盘",
            isActive = currentRoute.endsWith("/dashboard"),
            onClick = { navController.navigate(Screen.Dashboard) }
        )
        
        SidebarItem(
            icon = "📝",
            label = "博客管理",
            isActive = currentRoute.contains("/blogs") && currentRoute.contains("/admin"),
            onClick = { navController.navigate(Screen.BlogManage) }
        )
        
        SidebarItem(
            icon = "⚙️",
            label = "站点设置",
            isActive = currentRoute.endsWith("/site-config"),
            onClick = { navController.navigate(Screen.SiteConfig) }
        )
        
        SidebarItem(
            icon = "📈",
            label = "访问统计",
            isActive = currentRoute.endsWith("/statistics"),
            onClick = { navController.navigate(Screen.Statistics) }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(color = AppColors.Divider)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Actions
        SidebarItem(
            icon = "🏠",
            label = "返回前台",
            isActive = false,
            onClick = { navController.navigate(Screen.Home) }
        )
        
        SidebarItem(
            icon = "🚪",
            label = "退出登录",
            isActive = false,
            onClick = {
                Storage.clearAuth()
                navController.navigate(Screen.Home)
            }
        )
    }
}

/**
 * 侧边栏菜单项
 */
@Composable
private fun SidebarItem(
    icon: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = if (isActive) AppColors.Primary.copy(alpha = 0.1f) else AppColors.Surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isActive) AppColors.Primary else AppColors.TextSecondary
        )
    }
    
    Spacer(modifier = Modifier.height(4.dp))
}

/**
 * 后台管理布局
 */
@Composable
fun AdminLayout(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar
        AdminSidebar(
            navController = navController,
            currentRoute = navController.currentRoute
        )
        
        // Main Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Background)
                .padding(24.dp)
        ) {
            content()
        }
    }
}
