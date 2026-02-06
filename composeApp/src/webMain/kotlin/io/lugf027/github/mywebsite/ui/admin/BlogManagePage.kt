package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.dto.BlogStatus
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.blog.Pagination
import io.lugf027.github.mywebsite.ui.components.EmptyState
import io.lugf027.github.mywebsite.ui.components.ErrorState
import io.lugf027.github.mywebsite.ui.components.Loading
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AdminViewModel

/**
 * 博客管理页面
 */
@Composable
fun BlogManagePage(
    navController: NavController,
    viewModel: AdminViewModel = remember { AdminViewModel() }
) {
    var statusFilter by remember { mutableStateOf<String?>(null) }
    var searchKeyword by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<Long?>(null) }
    
    LaunchedEffect(Unit) {
        viewModel.loadBlogs()
    }
    
    AdminLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "博客管理",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    
                    Text(
                        text = "共 ${viewModel.total} 篇文章",
                        fontSize = 14.sp,
                        color = AppColors.TextTertiary
                    )
                }
                
                Button(
                    onClick = { navController.navigate(Screen.BlogEditor(null)) },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("✏️ 写博客")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search
                OutlinedTextField(
                    value = searchKeyword,
                    onValueChange = { searchKeyword = it },
                    placeholder = { Text("搜索文章...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.loadBlogs(1, statusFilter, searchKeyword) }) {
                            Text("🔍")
                        }
                    }
                )
                
                // Status Filter
                FilterChip(
                    onClick = { 
                        statusFilter = if (statusFilter == null) BlogStatus.PUBLISHED else null
                        viewModel.loadBlogs(1, statusFilter, searchKeyword)
                    },
                    label = { Text("已发布") },
                    selected = statusFilter == BlogStatus.PUBLISHED
                )
                
                FilterChip(
                    onClick = { 
                        statusFilter = if (statusFilter == BlogStatus.DRAFT) null else BlogStatus.DRAFT
                        viewModel.loadBlogs(1, statusFilter, searchKeyword)
                    },
                    label = { Text("草稿") },
                    selected = statusFilter == BlogStatus.DRAFT
                )
            }
            
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
                        subtitle = "点击上方按钮创建第一篇文章"
                    )
                }
                else -> {
                    // Blog Table
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AppColors.White)
                    ) {
                        Column {
                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("标题", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(2f))
                                Text("状态", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.5f))
                                Text("浏览", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.5f))
                                Text("创建时间", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Text("操作", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            }
                            
                            HorizontalDivider(color = AppColors.Divider)
                            
                            // Table Rows
                            viewModel.blogList.forEach { blog ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate(Screen.BlogEditor(blog.id)) }
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = blog.title,
                                        modifier = Modifier.weight(2f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    
                                    Text(
                                        text = if (blog.status == BlogStatus.PUBLISHED) "✅ 已发布" else "📝 草稿",
                                        modifier = Modifier.weight(0.5f),
                                        fontSize = 12.sp,
                                        color = if (blog.status == BlogStatus.PUBLISHED) AppColors.Success else AppColors.TextTertiary
                                    )
                                    
                                    Text(
                                        text = blog.viewCount.toString(),
                                        modifier = Modifier.weight(0.5f),
                                        fontSize = 14.sp,
                                        color = AppColors.TextSecondary
                                    )
                                    
                                    Text(
                                        text = blog.createdAt.take(10),
                                        modifier = Modifier.weight(1f),
                                        fontSize = 14.sp,
                                        color = AppColors.TextSecondary
                                    )
                                    
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TextButton(
                                            onClick = { navController.navigate(Screen.BlogEditor(blog.id)) }
                                        ) {
                                            Text("编辑", fontSize = 12.sp)
                                        }
                                        
                                        if (blog.status == BlogStatus.DRAFT) {
                                            TextButton(
                                                onClick = { 
                                                    viewModel.publishBlog(blog.id) { 
                                                        viewModel.loadBlogs(viewModel.currentPage, statusFilter, searchKeyword)
                                                    }
                                                }
                                            ) {
                                                Text("发布", fontSize = 12.sp, color = AppColors.Success)
                                            }
                                        } else {
                                            TextButton(
                                                onClick = { 
                                                    viewModel.unpublishBlog(blog.id) { 
                                                        viewModel.loadBlogs(viewModel.currentPage, statusFilter, searchKeyword)
                                                    }
                                                }
                                            ) {
                                                Text("下架", fontSize = 12.sp, color = AppColors.Warning)
                                            }
                                        }
                                        
                                        TextButton(
                                            onClick = { showDeleteDialog = blog.id }
                                        ) {
                                            Text("删除", fontSize = 12.sp, color = AppColors.Error)
                                        }
                                    }
                                }
                                HorizontalDivider(color = AppColors.Divider)
                            }
                        }
                    }
                    
                    // Pagination
                    if (viewModel.totalPages > 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Pagination(
                            currentPage = viewModel.currentPage,
                            totalPages = viewModel.totalPages,
                            onPageChange = { viewModel.loadBlogs(it, statusFilter, searchKeyword) }
                        )
                    }
                }
            }
        }
    }
    
    // Delete Confirmation Dialog
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("确认删除") },
            text = { Text("确定要删除这篇博客吗？此操作不可恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteBlog(showDeleteDialog!!) {
                            viewModel.loadBlogs(viewModel.currentPage, statusFilter, searchKeyword)
                        }
                        showDeleteDialog = null
                    }
                ) {
                    Text("删除", color = AppColors.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("取消")
                }
            }
        )
    }
}
