package io.lugf027.github.mywebsite.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.config.AppConfig
import io.lugf027.github.mywebsite.model.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * 数据库配置和初始化
 */
fun Application.configureDatabase() {
    DatabaseFactory.init()
}

object DatabaseFactory : Logger() {
    
    private lateinit var dataSource: HikariDataSource
    
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = AppConfig.Database.driverClassName
            jdbcUrl = AppConfig.Database.jdbcUrl
            username = AppConfig.Database.username
            password = AppConfig.Database.password
            maximumPoolSize = AppConfig.Database.maximumPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        
        dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        
        logger.info("Database connected: {}", AppConfig.Database.jdbcUrl)
        
        // 创建表结构
        transaction {
            SchemaUtils.create(
                Users,
                Blogs,
                SiteConfigs,
                AccessLogs
            )
            logger.info("Database tables created/verified")
        }
    }
    
    fun close() {
        if (::dataSource.isInitialized) {
            dataSource.close()
            logger.info("Database connection closed")
        }
    }
}
