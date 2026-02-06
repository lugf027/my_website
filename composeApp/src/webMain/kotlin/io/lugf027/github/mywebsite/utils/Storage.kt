package io.lugf027.github.mywebsite.utils

/**
 * 本地存储工具
 * 使用内存存储，实际的浏览器 localStorage 将在平台特定代码中处理
 */
object Storage {
    private const val TOKEN_KEY = "auth_token"
    private const val USER_KEY = "user_info"
    
    // 使用内存存储作为后备
    private val memoryStorage = mutableMapOf<String, String>()
    
    /**
     * 保存 Token
     */
    fun saveToken(token: String) {
        memoryStorage[TOKEN_KEY] = token
        BrowserStorage.setItem(TOKEN_KEY, token)
    }
    
    /**
     * 获取 Token
     */
    fun getToken(): String? {
        return memoryStorage[TOKEN_KEY] ?: BrowserStorage.getItem(TOKEN_KEY)?.also {
            memoryStorage[TOKEN_KEY] = it
        }
    }
    
    /**
     * 清除 Token
     */
    fun clearToken() {
        memoryStorage.remove(TOKEN_KEY)
        BrowserStorage.removeItem(TOKEN_KEY)
    }
    
    /**
     * 保存用户信息 JSON
     */
    fun saveUserJson(userJson: String) {
        memoryStorage[USER_KEY] = userJson
        BrowserStorage.setItem(USER_KEY, userJson)
    }
    
    /**
     * 获取用户信息 JSON
     */
    fun getUserJson(): String? {
        return memoryStorage[USER_KEY] ?: BrowserStorage.getItem(USER_KEY)?.also {
            memoryStorage[USER_KEY] = it
        }
    }
    
    /**
     * 清除用户信息
     */
    fun clearUser() {
        memoryStorage.remove(USER_KEY)
        BrowserStorage.removeItem(USER_KEY)
    }
    
    /**
     * 清除所有认证信息
     */
    fun clearAuth() {
        clearToken()
        clearUser()
    }
    
    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}

/**
 * 浏览器 localStorage 访问器
 * 使用 expect/actual 或运行时检测
 */
expect object BrowserStorage {
    fun setItem(key: String, value: String)
    fun getItem(key: String): String?
    fun removeItem(key: String)
}
