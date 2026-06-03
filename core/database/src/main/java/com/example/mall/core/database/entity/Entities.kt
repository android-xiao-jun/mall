package com.example.mall.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversations",
    indices = [
        Index(value = ["memberId"], unique = true),
        Index(value = ["lastMessageTime"]),
    ],
)
data class ConversationEntity(
    @PrimaryKey val id: String,
    val type: Int,
    val name: String,
    val avatar: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val memberId: String,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "messages",
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["createTime"]),
    ],
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String,
    val type: Int,
    val content: String,
    val extra: String,
    val status: Int,
    val createTime: Long,
)
