package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.components.Loading
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AdminViewModel

/**
 * 仪表盘页面
 */
@Composable
fun DashboardPage(
    navController: NavController,
    viewModel: AdminViewModel = remember { AdminViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.loadStatisticsOverview()
    }
    
    AdminLayout(navController = navController) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Page Title
            Text(
                text = "仪表盘",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "网站数据概览",
                fontSize = 14.sp,
                color = AppColors.TextTertiary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (viewModel.isLoading) {
                Loading()
            } else if (viewModel.statisticsOverview != null) {
                val stats = viewModel.statisticsOverview!!
                
                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsCard(
                        title = "总访问量",
                        value = stats.totalPv.toString(),
                        subtitle = "今日 ${stats.todayPv}",
                        icon = "👁",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatsCard(
                        title = "独立访客",
                        value = stats.totalUv.toString(),
                        subtitle = "今日 ${stats.todayUv}",
                        icon = "👤",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatsCard(
                        title = "博客文章",
                        value = stats.totalBlogs.toString(),
                        subtitle = "已发布 ${stats.publishedBlogs}",
                        icon = "📝",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatsCard(
                        title = "注册用户",
                        value = stats.totalUsers.toString(),
                        subtitle = "活跃用户",
                        icon = "👥",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Quick Actions
                Text(
                    text = "快捷操作",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuickActionButton(
                        icon = "✏️",
                        label = "写博客",
                        onClick = { navController.navigate(Screen.BlogEditor(null)) }
                    )
                    
                    QuickActionButton(
                        icon = "📋",
                        label = "管理博客",
                        onClick = { navController.navigate(Screen.BlogManage) }
                    )
                    
                    QuickActionButton(
                        icon = "⚙️",
                        label = "站点设置",
                        onClick = { navController.navigate(Screen.SiteConfig) }
                    )
                    
                    QuickActionButton(
                        icon = "📊",
                        label = "查看统计",
                        onClick = { navController.navigate(Screen.Statistics) }
                    )
                }
            }
        }
    }
}

/**
 * 统计卡片
 */
@Composable
private fun StatsCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = AppColors.TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = AppColors.TextTertiary
            )
        }
    }
}

/**
 * 快捷操作按钮
 */
@Composable
private fun QuickActionButton(
    icon: String,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}
