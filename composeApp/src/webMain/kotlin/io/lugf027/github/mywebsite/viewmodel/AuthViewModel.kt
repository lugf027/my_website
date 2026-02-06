package io.lugf027.github.mywebsite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.lugf027.github.mywebsite.api.ApiClient
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.dto.*
import io.lugf027.github.mywebsite.utils.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * 认证 ViewModel
 */
class AuthViewModel {
    var isLoggedIn by mutableStateOf(Storage.isLoggedIn())
        private set
    
    var currentUser by mutableStateOf<UserInfo?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var error by mutableStateOf<String?>(null)
        private set
    
    var successMessage by mutableStateOf<String?>(null)
        private set
    
    private val scope = CoroutineScope(Dispatchers.Default)
    private val json = Json { ignoreUnknownKeys = true }
    
    init {
        // 恢复用户信息
        Storage.getUserJson()?.let { userJson ->
            currentUser = json.decodeFromString<UserInfo>(userJson)
        }
    }
    
    /**
     * 登录
     */
    fun login(username: String, password: String, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val request = LoginRequest(username, password)
            val response = ApiClient.post<LoginRequest, LoginResponse>(
                ApiRoutes.Auth.LOGIN,
                request
            )
            
            if (response.code == 0 && response.data != null) {
                Storage.saveToken(response.data!!.token)
                Storage.saveUserJson(json.encodeToString(response.data!!.user))
                currentUser = response.data!!.user
                isLoggedIn = true
                successMessage = "登录成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 注册
     */
    fun register(username: String, password: String, email: String, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val request = RegisterRequest(username, password, email)
            val response = ApiClient.post<RegisterRequest, UserInfo>(
                ApiRoutes.Auth.REGISTER,
                request
            )
            
            if (response.code == 0) {
                successMessage = "注册成功，请登录"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 登出
     */
    fun logout(onComplete: () -> Unit) {
        Storage.clearAuth()
        isLoggedIn = false
        currentUser = null
        onComplete()
    }
    
    /**
     * 清除错误消息
     */
    fun clearError() {
        error = null
    }
    
    /**
     * 清除成功消息
     */
    fun clearSuccessMessage() {
        successMessage = null
    }
}
