package io.lugf027.github.mywebsite.common

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.lugf027.github.mywebsite.dto.ApiResponse

/**
 * 统一响应工具
 */
object Response {
    
    /**
     * 成功响应
     */
    suspend inline fun <reified T> ApplicationCall.success(data: T? = null, message: String = "success") {
        respond(HttpStatusCode.OK, ApiResponse.success(data, message))
    }
    
    /**
     * 创建成功响应
     */
    suspend inline fun <reified T> ApplicationCall.created(data: T? = null, message: String = "Created successfully") {
        respond(HttpStatusCode.Created, ApiResponse.success(data, message))
    }
    
    /**
     * 错误响应
     */
    suspend fun ApplicationCall.error(
        httpStatusCode: HttpStatusCode,
        errorCode: Int,
        message: String
    ) {
        respond(httpStatusCode, ApiResponse.error<Unit>(errorCode, message))
    }
    
    /**
     * 400 Bad Request
     */
    suspend fun ApplicationCall.badRequest(errorCode: Int, message: String) {
        error(HttpStatusCode.BadRequest, errorCode, message)
    }
    
    /**
     * 401 Unauthorized
     */
    suspend fun ApplicationCall.unauthorized(message: String = "Unauthorized") {
        error(HttpStatusCode.Unauthorized, ErrorCode.UNAUTHORIZED, message)
    }
    
    /**
     * 403 Forbidden
     */
    suspend fun ApplicationCall.forbidden(message: String = "Forbidden") {
        error(HttpStatusCode.Forbidden, ErrorCode.FORBIDDEN, message)
    }
    
    /**
     * 404 Not Found
     */
    suspend fun ApplicationCall.notFound(message: String = "Resource not found") {
        error(HttpStatusCode.NotFound, ErrorCode.NOT_FOUND, message)
    }
    
    /**
     * 500 Internal Server Error
     */
    suspend fun ApplicationCall.serverError(message: String = "Internal server error") {
        error(HttpStatusCode.InternalServerError, ErrorCode.UNKNOWN_ERROR, message)
    }
}
