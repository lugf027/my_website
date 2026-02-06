package io.lugf027.github.mywebsite.utils

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * JavaScript 互操作工具
 * 注意：Kotlin/Wasm 对 js() 调用有严格限制
 */
object JsInterop {
    
    /**
     * 渲染 Markdown 为 HTML
     * 在 Wasm 环境中，使用简单的文本返回，实际渲染由 HTML 组件处理
     */
    fun renderMarkdown(markdown: String): String {
        // 在 Wasm 环境中，Markdown 渲染应该在 HTML 层面处理
        // 这里返回原始内容，由 MarkdownViewer 组件使用 HtmlText 渲染
        return markdown
    }
    
    /**
     * 高亮代码
     * 代码高亮应该在页面加载后由 JavaScript 处理
     */
    fun highlightCode() {
        // 在 Wasm 中无法直接调用 hljs
        // 代码高亮应该通过 index.html 中的脚本处理
    }
    
    /**
     * 获取当前 URL 路径
     */
    fun getCurrentPath(): String {
        return window.location.pathname
    }
    
    /**
     * 获取 URL 参数
     */
    fun getQueryParam(name: String): String? {
        val search = window.location.search
        if (search.isEmpty()) return null
        
        val params = search.removePrefix("?").split("&")
        for (param in params) {
            val parts = param.split("=", limit = 2)
            if (parts.size == 2 && parts[0] == name) {
                return decodeURIComponent(parts[1])
            }
        }
        return null
    }
    
    /**
     * URL 解码
     */
    private fun decodeURIComponent(encoded: String): String {
        return encoded
            .replace("+", " ")
            .replace("%20", " ")
            .replace("%21", "!")
            .replace("%22", "\"")
            .replace("%23", "#")
            .replace("%24", "$")
            .replace("%25", "%")
            .replace("%26", "&")
            .replace("%27", "'")
            .replace("%28", "(")
            .replace("%29", ")")
            .replace("%2F", "/")
            .replace("%3A", ":")
            .replace("%3B", ";")
            .replace("%3D", "=")
            .replace("%3F", "?")
            .replace("%40", "@")
    }
    
    /**
     * 导航到新路径
     */
    fun navigateTo(path: String) {
        window.history.pushState(null, "", path)
        // 触发自定义事件通知路由变化
        val event = document.createEvent("Event")
        event.initEvent("routechange", true, true)
        window.dispatchEvent(event)
    }
    
    /**
     * 滚动到顶部
     */
    fun scrollToTop() {
        window.scrollTo(0.0, 0.0)
    }
    
    /**
     * 复制文本到剪贴板
     */
    fun copyToClipboard(text: String) {
        window.navigator.clipboard.writeText(text)
    }
    
    /**
     * 打开新窗口
     */
    fun openInNewTab(url: String) {
        window.open(url, "_blank")
    }
    
    /**
     * 获取 URL hash
     */
    fun getHash(): String {
        return window.location.hash.removePrefix("#")
    }
    
    /**
     * 设置 URL hash
     */
    fun setHash(hash: String) {
        window.location.hash = hash
    }
}
