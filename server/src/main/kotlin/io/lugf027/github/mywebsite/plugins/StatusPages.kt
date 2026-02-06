package io.lugf027.github.mywebsite.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.lugf027.github.mywebsite.common.*
import io.lugf027.github.mywebsite.dto.ApiResponse

/**
 * 统一异常处理配置
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        // 处理自定义业务异常
        exception<AppException> { call, cause ->
            val httpStatus = when (cause) {
                is InvalidParameterException -> HttpStatusCode.BadRequest
                is NotFoundException, is UserNotFoundException, is BlogNotFoundException -> HttpStatusCode.NotFound
                is UnauthorizedException, is InvalidCredentialsException -> HttpStatusCode.Unauthorized
                is ForbiddenException -> HttpStatusCode.Forbidden
                is DatabaseException -> HttpStatusCode.InternalServerError
                else -> HttpStatusCode.InternalServerError
            }
            
            call.respond(
                httpStatus,
                ApiResponse.error<Unit>(cause.errorCode, cause.message)
            )
        }
        
        // 处理参数验证异常
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse.error<Unit>(ErrorCode.INVALID_PARAMETER, cause.message ?: "Invalid parameter")
            )
        }
        
        // 处理其他未捕获异常
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse.error<Unit>(ErrorCode.UNKNOWN_ERROR, "Internal server error")
            )
        }
        
        // HTTP 状态码处理
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ApiResponse.error<Unit>(ErrorCode.NOT_FOUND, "Resource not found")
            )
        }
        
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respond(
                status,
                ApiResponse.error<Unit>(ErrorCode.UNAUTHORIZED, "Unauthorized")
            )
        }
    }
}
