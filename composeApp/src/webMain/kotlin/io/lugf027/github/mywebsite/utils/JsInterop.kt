package io.lugf027.github.mywebsite.utils

/**
 * JavaScript 互操作工具
 * 使用 expect/actual 模式支持 JS 和 WasmJS 平台
 */
object JsInterop {
    
    /**
     * 渲染 Markdown 为 HTML
     * 在 Wasm 环境中，Markdown 渲染应该在 HTML 层面处理
     */
    fun renderMarkdown(markdown: String): String {
        // Markdown 渲染由前端 JavaScript 库处理
        return markdown
    }
    
    /**
     * 高亮代码
     * 代码高亮应该在页面加载后由 JavaScript 处理
     */
    fun highlightCode() {
        // 代码高亮由前端 JavaScript 库处理
    }
    
    /**
     * 获取当前 URL 路径
     */
    fun getCurrentPath(): String {
        return BrowserWindow.getPathname()
    }
    
    /**
     * 获取 URL 参数
     */
    fun getQueryParam(name: String): String? {
        val search = BrowserWindow.getSearch()
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
        BrowserWindow.pushState(path)
    }
    
    /**
     * 滚动到顶部
     */
    fun scrollToTop() {
        BrowserWindow.scrollTo(0.0, 0.0)
    }
    
    /**
     * 复制文本到剪贴板
     */
    fun copyToClipboard(text: String) {
        BrowserWindow.copyToClipboard(text)
    }
    
    /**
     * 打开新窗口
     */
    fun openInNewTab(url: String) {
        BrowserWindow.openUrl(url)
    }
    
    /**
     * 获取 URL hash
     */
    fun getHash(): String {
        return BrowserWindow.getHash().removePrefix("#")
    }
    
    /**
     * 设置 URL hash
     */
    fun setHash(hash: String) {
        BrowserWindow.setHash(hash)
    }
}

/**
 * 浏览器 Window 访问器
 * 使用 expect/actual 模式支持不同平台
 */
expect object BrowserWindow {
    fun getPathname(): String
    fun getSearch(): String
    fun getHash(): String
    fun setHash(hash: String)
    fun pushState(path: String)
    fun replaceState(path: String)
    fun goBack()
    fun scrollTo(x: Double, y: Double)
    fun copyToClipboard(text: String)
    fun openUrl(url: String)
    fun addPopStateListener(callback: (Any?) -> Unit)
}

/**
 * 浏览器 DOM 访问器
 */
expect object BrowserDom {
    fun setInnerHtml(elementId: String, html: String)
    fun createDivWithHtml(html: String): String
}
