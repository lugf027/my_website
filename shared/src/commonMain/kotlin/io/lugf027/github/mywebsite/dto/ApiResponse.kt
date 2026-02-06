package io.lugf027.github.mywebsite.dto

import kotlinx.serialization.Serializable

/**
 * 统一 API 响应格式
 */
@Serializable
data class ApiResponse<T>(
    val code: Int = 0,
    val message: String = "success",
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "success"): ApiResponse<T> {
            return ApiResponse(code = 0, message = message, data = data)
        }

        fun <T> error(code: Int, message: String): ApiResponse<T> {
            return ApiResponse(code = code, message = message, data = null)
        }
    }
}

/**
 * 分页响应数据
 */
@Serializable
data class PageData<T>(
    val items: List<T>,
    val total: Long,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int
)
