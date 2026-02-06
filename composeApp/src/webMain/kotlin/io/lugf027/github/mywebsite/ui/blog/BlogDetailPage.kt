package io.lugf027.github.mywebsite.ui.blog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.components.*
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.BlogViewModel

/**
 * 博客详情页面
 */
@Composable
fun BlogDetailPage(
    navController: NavController,
    blogId: Long,
    viewModel: BlogViewModel = remember { BlogViewModel() }
) {
    LaunchedEffect(blogId) {
        viewModel.loadBlogDetail(blogId)
    }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Header
        Header(navController = navController)
        
        // Content
        when {
            viewModel.isLoading -> {
                FullScreenLoading(message = "加载文章中...")
            }
            viewModel.error != null -> {
                ErrorState(
                    message = viewModel.error!!,
                    onRetry = { viewModel.loadBlogDetail(blogId) }
                )
            }
            viewModel.blogDetail != null -> {
                val blog = viewModel.blogDetail!!
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Back Button
                    TextButton(
                        onClick = { navController.navigate(Screen.BlogList) }
                    ) {
                        Text("← 返回博客列表", color = AppColors.Primary)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    Text(
                        text = blog.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Meta Info
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = "👤 ${blog.authorName}",
                            fontSize = 14.sp,
                            color = AppColors.TextSecondary
                        )
                        Text(
                            text = "📅 ${blog.publishedAt?.take(10) ?: blog.createdAt.take(10)}",
                            fontSize = 14.sp,
                            color = AppColors.TextSecondary
                        )
                        Text(
                            text = "👁 ${blog.viewCount} 次阅读",
                            fontSize = 14.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    HorizontalDivider(color = AppColors.Divider)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Markdown Content
                    SimpleMarkdownPreview(
                        markdown = blog.content,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    HorizontalDivider(color = AppColors.Divider)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Bottom Navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { navController.navigate(Screen.BlogList) }
                        ) {
                            Text("← 返回列表", color = AppColors.Primary)
                        }
                        
                        TextButton(
                            onClick = { navController.navigate(Screen.Home) }
                        ) {
                            Text("返回首页 →", color = AppColors.Primary)
                        }
                    }
                }
            }
            else -> {
                EmptyState(title = "文章不存在")
            }
        }
        
        // Footer
        Footer()
    }
}
