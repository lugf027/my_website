package io.lugf027.github.mywebsite.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.lugf027.github.mywebsite.dto.ApiResponse
import io.lugf027.github.mywebsite.utils.Storage
import kotlinx.serialization.json.Json

/**
 * HTTP 客户端封装
 */
object ApiClient {
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }
    
    // API 基础 URL
    var baseUrl: String = "http://localhost:8080"
    
    /**
     * GET 请求
     */
    suspend inline fun <reified T> get(path: String, params: Map<String, String> = emptyMap()): ApiResponse<T> {
        val response = client.get(baseUrl + path) {
            params.forEach { (key, value) ->
                parameter(key, value)
            }
            applyHeaders()
        }
        return response.body()
    }
    
    /**
     * POST 请求
     */
    suspend inline fun <reified T, reified R> post(path: String, body: T): ApiResponse<R> {
        val response = client.post(baseUrl + path) {
            contentType(ContentType.Application.Json)
            setBody(body)
            applyHeaders()
        }
        return response.body()
    }
    
    /**
     * PUT 请求
     */
    suspend inline fun <reified T, reified R> put(path: String, body: T): ApiResponse<R> {
        val response = client.put(baseUrl + path) {
            contentType(ContentType.Application.Json)
            setBody(body)
            applyHeaders()
        }
        return response.body()
    }
    
    /**
     * DELETE 请求
     */
    suspend inline fun <reified T> delete(path: String): ApiResponse<T> {
        val response = client.delete(baseUrl + path) {
            applyHeaders()
        }
        return response.body()
    }
    
    /**
     * 应用请求头（包括认证 Token）
     */
    fun HttpRequestBuilder.applyHeaders() {
        Storage.getToken()?.let { token ->
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}
