package io.lugf027.github.mywebsite.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.lugf027.github.mywebsite.utils.JsInterop
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

/**
 * Markdown 渲染组件
 * 使用 marked.js 将 Markdown 转换为 HTML
 */
@Composable
fun MarkdownViewer(
    markdown: String,
    modifier: Modifier = Modifier
) {
    var htmlContent by remember { mutableStateOf("") }
    
    // 渲染 Markdown
    LaunchedEffect(markdown) {
        htmlContent = JsInterop.renderMarkdown(markdown)
    }
    
    // 使用 DOM 元素显示 HTML
    DisposableEffect(htmlContent) {
        val container = document.getElementById("markdown-container") as? HTMLDivElement
        container?.innerHTML = htmlContent
        
        // 高亮代码块
        JsInterop.highlightCode()
        
        onDispose { }
    }
    
    // Compose 中的占位符，实际内容通过 JS 渲染
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 这里使用 HTML 渲染，所以 Compose 只是提供布局容器
        // 实际的 Markdown 内容会通过 JS 注入到 DOM 中
    }
}

/**
 * 简单的 Markdown 预览（纯 Compose 实现，支持基本语法）
 */
@Composable
fun SimpleMarkdownPreview(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val lines = markdown.split("\n")
    
    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        lines.forEach { line ->
            when {
                line.startsWith("# ") -> {
                    androidx.compose.material3.Text(
                        text = line.removePrefix("# "),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
                    )
                }
                line.startsWith("## ") -> {
                    androidx.compose.material3.Text(
                        text = line.removePrefix("## "),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                    )
                }
                line.startsWith("### ") -> {
                    androidx.compose.material3.Text(
                        text = line.removePrefix("### "),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                    )
                }
                line.startsWith("- ") || line.startsWith("* ") -> {
                    androidx.compose.material3.Text(
                        text = "• ${line.drop(2)}",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                    )
                }
                line.isBlank() -> {
                    androidx.compose.foundation.layout.Spacer(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                else -> {
                    androidx.compose.material3.Text(
                        text = line,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
