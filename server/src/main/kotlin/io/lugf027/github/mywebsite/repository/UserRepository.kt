package io.lugf027.github.mywebsite.repository

import io.lugf027.github.mywebsite.common.Logger
import io.lugf027.github.mywebsite.model.entities.User
import io.lugf027.github.mywebsite.model.tables.Users
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

/**
 * 用户数据访问层
 */
class UserRepository : Logger() {
    
    /**
     * 根据用户名查找用户
     */
    fun findByUsername(username: String): User? = transaction {
        User.find { Users.username eq username }.firstOrNull()
    }
    
    /**
     * 根据邮箱查找用户
     */
    fun findByEmail(email: String): User? = transaction {
        User.find { Users.email eq email }.firstOrNull()
    }
    
    /**
     * 根据 ID 查找用户
     */
    fun findById(id: Long): User? = transaction {
        User.findById(id)
    }
    
    /**
     * 创建用户
     */
    fun create(username: String, passwordHash: String, email: String, role: String = "user"): User = transaction {
        User.new {
            this.username = username
            this.passwordHash = passwordHash
            this.email = email
            this.role = role
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }.also {
            logger.info("User created: {}", username)
        }
    }
    
    /**
     * 更新用户密码
     */
    fun updatePassword(id: Long, passwordHash: String): Boolean = transaction {
        User.findById(id)?.let {
            it.passwordHash = passwordHash
            it.updatedAt = LocalDateTime.now()
            true
        } ?: false
    }
    
    /**
     * 检查用户名是否存在
     */
    fun existsByUsername(username: String): Boolean = transaction {
        User.find { Users.username eq username }.count() > 0
    }
    
    /**
     * 检查邮箱是否存在
     */
    fun existsByEmail(email: String): Boolean = transaction {
        User.find { Users.email eq email }.count() > 0
    }
    
    /**
     * 获取用户总数
     */
    fun count(): Long = transaction {
        User.count()
    }
    
    /**
     * 获取所有用户
     */
    fun findAll(): List<User> = transaction {
        User.all().toList()
    }
}
