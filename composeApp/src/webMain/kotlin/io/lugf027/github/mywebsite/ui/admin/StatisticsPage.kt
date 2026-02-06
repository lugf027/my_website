package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.ui.components.Loading
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AdminViewModel

/**
 * 统计报表页面
 */
@Composable
fun StatisticsPage(
    navController: NavController,
    viewModel: AdminViewModel = remember { AdminViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.loadStatisticsReport()
    }
    
    AdminLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                text = "访问统计",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            
            Text(
                text = "网站访问数据分析",
                fontSize = 14.sp,
                color = AppColors.TextTertiary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (viewModel.isLoading) {
                Loading()
            } else if (viewModel.statisticsReport != null) {
                val report = viewModel.statisticsReport!!
                
                // Overview Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "总 PV",
                        value = report.overview.totalPv.toString(),
                        subtitle = "今日 ${report.overview.todayPv}",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatCard(
                        title = "总 UV",
                        value = report.overview.totalUv.toString(),
                        subtitle = "今日 ${report.overview.todayUv}",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatCard(
                        title = "文章数",
                        value = report.overview.totalBlogs.toString(),
                        subtitle = "已发布 ${report.overview.publishedBlogs}",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatCard(
                        title = "用户数",
                        value = report.overview.totalUsers.toString(),
                        subtitle = "注册用户",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Daily Stats Chart (简化为表格形式)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "📈 近期访问趋势",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Table Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("日期", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text("PV", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text("UV", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Table Rows
                        report.dailyStats.takeLast(7).forEach { stat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(stat.date, modifier = Modifier.weight(1f), color = AppColors.TextSecondary)
                                Text(stat.pv.toString(), modifier = Modifier.weight(1f))
                                Text(stat.uv.toString(), modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Popular Blogs
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AppColors.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "🔥 热门文章",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (report.popularBlogs.isEmpty()) {
                                Text("暂无数据", color = AppColors.TextTertiary)
                            } else {
                                report.popularBlogs.take(5).forEachIndexed { index, blog ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${index + 1}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (index < 3) AppColors.Primary else AppColors.TextTertiary
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = blog.title.take(20) + if (blog.title.length > 20) "..." else "",
                                                fontSize = 14.sp
                                            )
                                        }
                                        Text(
                                            text = "${blog.viewCount} 次",
                                            fontSize = 12.sp,
                                            color = AppColors.TextTertiary
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Device Stats
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AppColors.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "📱 设备分布",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (report.devices.isEmpty()) {
                                Text("暂无数据", color = AppColors.TextTertiary)
                            } else {
                                val total = report.devices.sumOf { it.count }.toFloat()
                                
                                report.devices.forEach { device ->
                                    val percentage = if (total > 0) (device.count / total * 100).toInt() else 0
                                    
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = when (device.device) {
                                                    "Mobile" -> "📱"
                                                    "Tablet" -> "📟"
                                                    else -> "💻"
                                                },
                                                fontSize = 16.sp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(device.device, fontSize = 14.sp)
                                        }
                                        Text(
                                            text = "$percentage%",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = AppColors.Primary
                                        )
                                    }
                                    
                                    LinearProgressIndicator(
                                        progress = { percentage / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp),
                                        color = AppColors.Primary,
                                        trackColor = AppColors.SurfaceVariant
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 统计卡片
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = AppColors.TextSecondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                fontSize = 28.sp,
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
