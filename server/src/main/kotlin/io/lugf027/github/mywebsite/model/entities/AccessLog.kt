package io.lugf027.github.mywebsite.model.entities

import io.lugf027.github.mywebsite.model.tables.AccessLogs
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 访问日志实体
 */
class AccessLog(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccessLog>(AccessLogs)
    
    var path by AccessLogs.path
    var method by AccessLogs.method
    var ip by AccessLogs.ip
    var userAgent by AccessLogs.userAgent
    var referer by AccessLogs.referer
    var userId by AccessLogs.userId
    var statusCode by AccessLogs.statusCode
    var duration by AccessLogs.duration
    var createdAt by AccessLogs.createdAt
}
