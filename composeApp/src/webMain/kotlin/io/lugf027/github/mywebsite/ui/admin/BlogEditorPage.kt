package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.dto.BlogRequest
import io.lugf027.github.mywebsite.dto.BlogStatus
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.components.FullScreenLoading
import io.lugf027.github.mywebsite.ui.components.SimpleMarkdownPreview
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AdminViewModel

/**
 * 博客编辑页面
 */
@Composable
fun BlogEditorPage(
    navController: NavController,
    blogId: Long? = null,
    viewModel: AdminViewModel = remember { AdminViewModel() }
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var showPreview by remember { mutableStateOf(false) }
    
    val isEditing = blogId != null
    
    // Load blog for editing
    LaunchedEffect(blogId) {
        if (blogId != null) {
            viewModel.loadBlogForEdit(blogId)
        }
    }
    
    // Update form when blog is loaded
    LaunchedEffect(viewModel.editingBlog) {
        viewModel.editingBlog?.let { blog ->
            title = blog.title
            content = blog.content
            summary = blog.summary
        }
    }
    
    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearEditingBlog()
        }
    }
    
    if (isEditing && viewModel.isLoading && viewModel.editingBlog == null) {
        FullScreenLoading(message = "加载文章...")
        return
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isEditing) "编辑博客" else "写博客",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    
                    TextButton(onClick = { navController.navigate(Screen.BlogManage) }) {
                        Text("← 返回博客管理", color = AppColors.Primary)
                    }
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showPreview = !showPreview },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (showPreview) "编辑" else "预览")
                    }
                    
                    OutlinedButton(
                        onClick = {
                            val request = BlogRequest(title, content, summary, BlogStatus.DRAFT)
                            if (isEditing) {
                                viewModel.updateBlog(blogId!!, request) {
                                    navController.navigate(Screen.BlogManage)
                                }
                            } else {
                                viewModel.createBlog(request) {
                                    navController.navigate(Screen.BlogManage)
                                }
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        enabled = !viewModel.isLoading && title.isNotBlank() && content.isNotBlank()
                    ) {
                        Text("保存草稿")
                    }
                    
                    Button(
                        onClick = {
                            val request = BlogRequest(title, content, summary, BlogStatus.PUBLISHED)
                            if (isEditing) {
                                viewModel.updateBlog(blogId!!, request) {
                                    navController.navigate(Screen.BlogManage)
                                }
                            } else {
                                viewModel.createBlog(request) {
                                    navController.navigate(Screen.BlogManage)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !viewModel.isLoading && title.isNotBlank() && content.isNotBlank()
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = AppColors.White
                            )
                        } else {
                            Text("发布")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Error Message
            viewModel.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.Error.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error,
                        color = AppColors.Error,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (showPreview) {
                // Preview Mode
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = title.ifBlank { "无标题" },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        HorizontalDivider(color = AppColors.Divider)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        SimpleMarkdownPreview(
                            markdown = content.ifBlank { "无内容" }
                        )
                    }
                }
            } else {
                // Edit Mode
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题") },
                    placeholder = { Text("请输入文章标题...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Summary Input
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("摘要（可选）") },
                    placeholder = { Text("请输入文章摘要，留空将自动生成...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Content Input
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("内容（支持 Markdown）") },
                    placeholder = { Text("请输入文章内容...\n\n支持 Markdown 语法：\n# 标题\n## 二级标题\n**粗体** *斜体*\n- 列表项\n```代码块```") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Markdown Help
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.SurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📝 Markdown 语法提示",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = """
                                # 一级标题    ## 二级标题    ### 三级标题
                                **粗体**    *斜体*    ~~删除线~~
                                [链接文字](URL)    ![图片描述](图片URL)
                                `行内代码`    ```代码块```
                                - 无序列表    1. 有序列表
                                > 引用文字
                            """.trimIndent(),
                            fontSize = 12.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}
