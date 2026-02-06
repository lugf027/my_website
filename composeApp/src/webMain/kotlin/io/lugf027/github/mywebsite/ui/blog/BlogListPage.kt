package io.lugf027.github.mywebsite.ui.blog

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
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.components.*
import io.lugf027.github.mywebsite.ui.home.BlogCard
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.BlogViewModel

/**
 * 博客列表页面
 */
@Composable
fun BlogListPage(
    navController: NavController,
    viewModel: BlogViewModel = remember { BlogViewModel() }
) {
    var searchKeyword by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        viewModel.loadBlogs()
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Page Title
            Text(
                text = "博客文章",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "共 ${viewModel.total} 篇文章",
                fontSize = 14.sp,
                color = AppColors.TextTertiary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = { searchKeyword = it },
                placeholder = { Text("搜索文章...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { viewModel.search(searchKeyword) }) {
                        Text("🔍")
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Blog List
            when {
                viewModel.isLoading -> {
                    Loading()
                }
                viewModel.error != null -> {
                    ErrorState(
                        message = viewModel.error!!,
                        onRetry = { viewModel.loadBlogs() }
                    )
                }
                viewModel.blogList.isEmpty() -> {
                    EmptyState(
                        title = "暂无文章",
                        subtitle = "还没有发布任何文章"
                    )
                }
                else -> {
                    viewModel.blogList.forEach { blog ->
                        BlogCard(
                            blog = blog,
                            onClick = { navController.navigate(Screen.BlogDetail(blog.id)) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    // Pagination
                    if (viewModel.totalPages > 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Pagination(
                            currentPage = viewModel.currentPage,
                            totalPages = viewModel.totalPages,
                            onPageChange = { viewModel.goToPage(it) }
                        )
                    }
                }
            }
        }
        
        // Footer
        Footer()
    }
}

/**
 * 分页组件
 */
@Composable
fun Pagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous
        TextButton(
            onClick = { onPageChange(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Text("← 上一页")
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Page Numbers
        val pageRange = getPageRange(currentPage, totalPages)
        pageRange.forEach { page ->
            if (page == -1) {
                Text("...", modifier = Modifier.padding(horizontal = 8.dp))
            } else {
                TextButton(
                    onClick = { onPageChange(page) }
                ) {
                    Text(
                        text = page.toString(),
                        color = if (page == currentPage) AppColors.Primary else AppColors.TextSecondary,
                        fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Next
        TextButton(
            onClick = { onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Text("下一页 →")
        }
    }
}

/**
 * 计算显示的页码范围
 */
private fun getPageRange(current: Int, total: Int): List<Int> {
    if (total <= 7) {
        return (1..total).toList()
    }
    
    return when {
        current <= 4 -> listOf(1, 2, 3, 4, 5, -1, total)
        current >= total - 3 -> listOf(1, -1, total - 4, total - 3, total - 2, total - 1, total)
        else -> listOf(1, -1, current - 1, current, current + 1, -1, total)
    }
}
