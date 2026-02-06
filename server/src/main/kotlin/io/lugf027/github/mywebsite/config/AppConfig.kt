package io.lugf027.github.mywebsite.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import java.io.File
import java.util.Properties

/**
 * 应用配置管理
 * 
 * 敏感配置从环境变量读取，本地开发时会自动加载 local.properties
 */
object AppConfig {
    
    init {
        // 在加载 HOCON 配置前，先加载 local.properties 到系统属性
        loadLocalProperties()
    }
    
    private val config = HoconApplicationConfig(ConfigFactory.load())
    
    /**
     * 加载 local.properties 文件中的配置到系统属性
     * 这样 HOCON 的 ${?ENV_VAR} 语法就能读取到这些值
     */
    private fun loadLocalProperties() {
        // 尝试从多个位置查找 local.properties
        val possiblePaths = listOf(
            "local.properties",                    // 项目根目录
            "../local.properties",                 // 从 server 目录运行时
            "../../local.properties",              // 从更深层目录运行时
            System.getProperty("user.dir") + "/local.properties"
        )
        
        for (path in possiblePaths) {
            val file = File(path)
            if (file.exists() && file.isFile) {
                try {
                    val properties = Properties()
                    file.inputStream().use { properties.load(it) }
                    
                    // 将属性设置为系统环境变量（仅当环境变量未设置时）
                    properties.forEach { (key, value) ->
                        val keyStr = key.toString()
                        val valueStr = value.toString()
                        
                        // 只有当环境变量未设置时才使用 local.properties 中的值
                        if (System.getenv(keyStr) == null) {
                            System.setProperty(keyStr, valueStr)
                        }
                    }
                    
                    println("✅ Loaded local.properties from: ${file.absolutePath}")
                    return
                } catch (e: Exception) {
                    System.err.println("⚠️ Failed to load local.properties: ${e.message}")
                }
            }
        }
        
        println("ℹ️ No local.properties found, using environment variables only")
    }
    
    /**
     * 从系统属性或环境变量获取配置值
     */
    private fun getEnvOrProperty(key: String, default: String? = null): String? {
        return System.getenv(key) ?: System.getProperty(key) ?: default
    }
    
    /**
     * 数据库配置
     */
    object Database {
        val driverClassName: String by lazy {
            getEnvOrProperty("DATABASE_DRIVER", "com.mysql.cj.jdbc.Driver")!!
        }
        
        val jdbcUrl: String by lazy {
            getEnvOrProperty("DATABASE_URL")
                ?: throw IllegalStateException("DATABASE_URL is not configured. Please set it in environment variables or local.properties")
        }
        
        val username: String by lazy {
            getEnvOrProperty("DATABASE_USER")
                ?: throw IllegalStateException("DATABASE_USER is not configured. Please set it in environment variables or local.properties")
        }
        
        val password: String by lazy {
            getEnvOrProperty("DATABASE_PASSWORD")
                ?: throw IllegalStateException("DATABASE_PASSWORD is not configured. Please set it in environment variables or local.properties")
        }
        
        val maximumPoolSize: Int by lazy {
            getEnvOrProperty("DATABASE_POOL_SIZE", "10")?.toIntOrNull() ?: 10
        }
    }
    
    /**
     * JWT 配置
     */
    object Jwt {
        val secret: String by lazy {
            getEnvOrProperty("JWT_SECRET")
                ?: throw IllegalStateException("JWT_SECRET is not configured. Please set it in environment variables or local.properties")
        }
        
        val issuer: String by lazy {
            config.propertyOrNull("jwt.issuer")?.getString() ?: "mywebsite"
        }
        
        val audience: String by lazy {
            config.propertyOrNull("jwt.audience")?.getString() ?: "mywebsite-users"
        }
        
        val realm: String by lazy {
            config.propertyOrNull("jwt.realm")?.getString() ?: "MyWebsite"
        }
        
        val expiryHours: Long by lazy {
            config.propertyOrNull("jwt.expiryHours")?.getString()?.toLongOrNull() ?: 24L
        }
    }
    
    /**
     * 服务器配置
     */
    object Server {
        val port: Int by lazy {
            getEnvOrProperty("PORT", "8080")?.toIntOrNull() ?: 8080
        }
    }
}
