package io.lugf027.github.mywebsite.utils

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

/**
 * 本地存储工具
 */
object Storage {
    private const val TOKEN_KEY = "auth_token"
    private const val USER_KEY = "user_info"
    
    /**
     * 保存 Token
     */
    fun saveToken(token: String) {
        localStorage[TOKEN_KEY] = token
    }
    
    /**
     * 获取 Token
     */
    fun getToken(): String? {
        return localStorage[TOKEN_KEY]
    }
    
    /**
     * 清除 Token
     */
    fun clearToken() {
        localStorage.removeItem(TOKEN_KEY)
    }
    
    /**
     * 保存用户信息 JSON
     */
    fun saveUserJson(userJson: String) {
        localStorage[USER_KEY] = userJson
    }
    
    /**
     * 获取用户信息 JSON
     */
    fun getUserJson(): String? {
        return localStorage[USER_KEY]
    }
    
    /**
     * 清除用户信息
     */
    fun clearUser() {
        localStorage.removeItem(USER_KEY)
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
