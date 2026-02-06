package io.lugf027.github.mywebsite.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.dto.BlogListItem
import io.lugf027.github.mywebsite.dto.SiteOverview
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.components.*
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.utils.JsInterop
import io.lugf027.github.mywebsite.viewmodel.HomeViewModel

/**
 * 首页
 */
@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomeViewModel = remember { HomeViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Header
        Header(navController = navController, siteName = viewModel.siteOverview?.siteName ?: "MyWebsite")
        
        // Main Content
        if (viewModel.isLoading && viewModel.siteOverview == null) {
            FullScreenLoading()
        } else {
            // Hero Section - Personal Introduction
            HeroSection(
                overview = viewModel.siteOverview,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Latest Blogs Section
            LatestBlogsSection(
                blogs = viewModel.latestBlogs,
                onBlogClick = { blogId ->
                    navController.navigate(Screen.BlogDetail(blogId))
                },
                onViewAllClick = {
                    navController.navigate(Screen.BlogList)
                }
            )
            
            // Footer
            Footer(
                icpRecord = viewModel.siteOverview?.icpRecord ?: "",
                copyright = "© 2024 ${viewModel.siteOverview?.ownerName ?: "MyWebsite"}. All rights reserved."
            )
        }
    }
}

/**
 * 个人介绍区域
 */
@Composable
private fun HeroSection(
    overview: SiteOverview?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        AppColors.Primary.copy(alpha = 0.05f),
                        AppColors.Background
                    )
                )
            )
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(AppColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = overview?.ownerName?.firstOrNull()?.toString() ?: "?",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Name
        Text(
            text = overview?.ownerName ?: "Your Name",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Title
        Text(
            text = overview?.ownerTitle ?: "Developer",
            fontSize = 18.sp,
            color = AppColors.Primary,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Bio
        Text(
            text = overview?.ownerBio ?: "Hello, welcome to my website!",
            fontSize = 16.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 600.dp).padding(horizontal = 24.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Social Links
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!overview?.githubUrl.isNullOrBlank()) {
                SocialButton(
                    icon = "🐙",
                    label = "GitHub",
                    onClick = { JsInterop.openInNewTab(overview!!.githubUrl) }
                )
            }
            if (!overview?.linkedinUrl.isNullOrBlank()) {
                SocialButton(
                    icon = "💼",
                    label = "LinkedIn",
                    onClick = { JsInterop.openInNewTab(overview!!.linkedinUrl) }
                )
            }
            if (!overview?.email.isNullOrBlank()) {
                SocialButton(
                    icon = "📧",
                    label = "Email",
                    onClick = { JsInterop.openInNewTab("mailto:${overview!!.email}") }
                )
            }
        }
    }
}

/**
 * 社交按钮
 */
@Composable
private fun SocialButton(
    icon: String,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AppColors.TextPrimary
        )
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

/**
 * 最新博客区域
 */
@Composable
private fun LatestBlogsSection(
    blogs: List<BlogListItem>,
    onBlogClick: (Long) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "最新文章",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            
            TextButton(onClick = onViewAllClick) {
                Text("查看全部 →", color = AppColors.Primary)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (blogs.isEmpty()) {
            EmptyState(
                title = "暂无文章",
                subtitle = "敬请期待..."
            )
        } else {
            // Blog Cards
            blogs.forEach { blog ->
                BlogCard(
                    blog = blog,
                    onClick = { onBlogClick(blog.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * 博客卡片
 */
@Composable
fun BlogCard(
    blog: BlogListItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Title
            Text(
                text = blog.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Summary
            Text(
                text = blog.summary,
                fontSize = 14.sp,
                color = AppColors.TextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Meta info
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "👤 ${blog.authorName}",
                    fontSize = 12.sp,
                    color = AppColors.TextTertiary
                )
                Text(
                    text = "📅 ${blog.createdAt.take(10)}",
                    fontSize = 12.sp,
                    color = AppColors.TextTertiary
                )
                Text(
                    text = "👁 ${blog.viewCount}",
                    fontSize = 12.sp,
                    color = AppColors.TextTertiary
                )
            }
        }
    }
}
