package io.lugf027.github.mywebsite.utils

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * JS 平台的浏览器 Window 实现
 */
actual object BrowserWindow {
    actual fun getPathname(): String {
        return window.location.pathname
    }
    
    actual fun getSearch(): String {
        return window.location.search
    }
    
    actual fun getHash(): String {
        return window.location.hash
    }
    
    actual fun setHash(hash: String) {
        window.location.hash = hash
    }
    
    actual fun pushState(path: String) {
        window.history.pushState(null, "", path)
    }
    
    actual fun replaceState(path: String) {
        window.history.replaceState(null, "", path)
    }
    
    actual fun goBack() {
        window.history.back()
    }
    
    actual fun scrollTo(x: Double, y: Double) {
        window.scrollTo(x, y)
    }
    
    actual fun copyToClipboard(text: String) {
        window.navigator.clipboard.writeText(text)
    }
    
    actual fun openUrl(url: String) {
        window.open(url, "_blank")
    }
    
    actual fun addPopStateListener(callback: (Any?) -> Unit) {
        window.onpopstate = { event ->
            callback(event)
        }
    }
}

/**
 * JS 平台的浏览器 DOM 实现
 */
actual object BrowserDom {
    actual fun setInnerHtml(elementId: String, html: String) {
        document.getElementById(elementId)?.innerHTML = html
    }
    
    actual fun createDivWithHtml(html: String): String {
        return html
    }
}
