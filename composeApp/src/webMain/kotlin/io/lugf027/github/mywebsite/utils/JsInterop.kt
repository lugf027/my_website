package io.lugf027.github.mywebsite.utils

import kotlinx.browser.window

/**
 * JavaScript 互操作工具
 */
object JsInterop {
    
    /**
     * 渲染 Markdown 为 HTML（使用 marked.js）
     */
    fun renderMarkdown(markdown: String): String {
        return js("typeof marked !== 'undefined' ? marked.parse(markdown) : markdown") as String
    }
    
    /**
     * 高亮代码（使用 highlight.js）
     */
    fun highlightCode() {
        js("if (typeof hljs !== 'undefined') { hljs.highlightAll(); }")
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
        val urlParams = js("new URLSearchParams(window.location.search)")
        return urlParams.get(name) as? String
    }
    
    /**
     * 导航到新路径
     */
    fun navigateTo(path: String) {
        window.history.pushState(null, "", path)
        // 触发 popstate 事件以便路由器能够响应
        window.dispatchEvent(js("new PopStateEvent('popstate')") as org.w3c.dom.events.Event)
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
}
