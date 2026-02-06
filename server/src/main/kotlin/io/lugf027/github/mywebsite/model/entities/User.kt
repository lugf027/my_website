package io.lugf027.github.mywebsite.model.entities

import io.lugf027.github.mywebsite.dto.UserInfo
import io.lugf027.github.mywebsite.model.tables.Users
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.format.DateTimeFormatter

/**
 * 用户实体
 */
class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users)
    
    var username by Users.username
    var passwordHash by Users.passwordHash
    var email by Users.email
    var role by Users.role
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt
    
    /**
     * 转换为 DTO
     */
    fun toUserInfo(): UserInfo {
        return UserInfo(
            id = id.value,
            username = username,
            email = email,
            role = role,
            createdAt = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}
