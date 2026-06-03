package com.example.mall.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mall.core.database.dao.ConversationDao
import com.example.mall.core.database.dao.MessageDao
import com.example.mall.core.database.entity.ConversationEntity
import com.example.mall.core.database.entity.MessageEntity
import com.example.mall.core.database.converter.RoomTypeConverters

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}
