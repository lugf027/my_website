package io.lugf027.github.mywebsite.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

/**
 * 应用配置管理
 */
object AppConfig {
    private val config = HoconApplicationConfig(ConfigFactory.load())
    
    /**
     * 数据库配置
     */
    object Database {
        val driverClassName: String by lazy {
            config.propertyOrNull("database.driverClassName")?.getString() 
                ?: "com.mysql.cj.jdbc.Driver"
        }
        
        val jdbcUrl: String by lazy {
            config.propertyOrNull("database.jdbcUrl")?.getString()
                ?: "jdbc:mysql://localhost:3306/mywebsite?useSSL=false&serverTimezone=UTC"
        }
        
        val username: String by lazy {
            config.propertyOrNull("database.username")?.getString() ?: "root"
        }
        
        val password: String by lazy {
            config.propertyOrNull("database.password")?.getString() ?: ""
        }
        
        val maximumPoolSize: Int by lazy {
            config.propertyOrNull("database.maximumPoolSize")?.getString()?.toIntOrNull() ?: 10
        }
    }
    
    /**
     * JWT 配置
     */
    object Jwt {
        val secret: String by lazy {
            config.propertyOrNull("jwt.secret")?.getString()
                ?: throw IllegalStateException("JWT secret is not configured")
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
            config.propertyOrNull("ktor.deployment.port")?.getString()?.toIntOrNull() ?: 8080
        }
    }
}
