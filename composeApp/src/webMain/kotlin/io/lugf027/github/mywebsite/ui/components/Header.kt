package io.lugf027.github.mywebsite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
 * 顶部导航栏
 */
@Composable
fun Header(
    navController: NavController,
    siteName: String = "MyWebsite"
) {
    val isLoggedIn = Storage.isLoggedIn()
    val currentRoute = navController.currentRoute
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo / Site Name
            Text(
                text = siteName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary,
                modifier = Modifier.clickable { navController.navigate(Screen.Home) }
            )
            
            // Navigation Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavLink(
                    text = "首页",
                    isActive = currentRoute == "/",
                    onClick = { navController.navigate(Screen.Home) }
                )
                
                NavLink(
                    text = "博客",
                    isActive = currentRoute.startsWith("/blogs"),
                    onClick = { navController.navigate(Screen.BlogList) }
                )
                
                if (isLoggedIn) {
                    NavLink(
                        text = "后台",
                        isActive = currentRoute.startsWith("/admin"),
                        onClick = { navController.navigate(Screen.Dashboard) }
                    )
                    
                    TextButton(
                        onClick = {
                            Storage.clearAuth()
                            navController.navigate(Screen.Home)
                        }
                    ) {
                        Text("退出", color = AppColors.TextSecondary)
                    }
                } else {
                    Button(
                        onClick = { navController.navigate(Screen.Login) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        )
                    ) {
                        Text("登录")
                    }
                }
            }
        }
    }
}

/**
 * 导航链接
 */
@Composable
private fun NavLink(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
        color = if (isActive) AppColors.Primary else AppColors.TextSecondary,
        modifier = Modifier.clickable(onClick = onClick)
    )
}
