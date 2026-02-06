package io.lugf027.github.mywebsite.utils

import kotlinx.browser.window

/**
 * WasmJS 平台的浏览器 localStorage 实现
 */
actual object BrowserStorage {
    actual fun setItem(key: String, value: String) {
        window.localStorage.setItem(key, value)
    }
    
    actual fun getItem(key: String): String? {
        return window.localStorage.getItem(key)
    }
    
    actual fun removeItem(key: String) {
        window.localStorage.removeItem(key)
    }
}
