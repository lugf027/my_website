package io.lugf027.github.mywebsite.model.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 博客表
 */
object Blogs : LongIdTable("blogs") {
    val title = varchar("title", 200)
    val content = text("content")
    val summary = varchar("summary", 500).default("")
    val status = varchar("status", 20).default("draft")
    val authorId = long("author_id").references(Users.id)
    val viewCount = integer("view_count").default(0)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val publishedAt = datetime("published_at").nullable()
    
    init {
        index(false, status)
        index(false, authorId)
        index(false, createdAt)
    }
}
