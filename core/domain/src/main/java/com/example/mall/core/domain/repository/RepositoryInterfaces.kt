package com.example.mall.core.domain.repository

import com.example.mall.core.common.result.Result
import com.example.mall.core.model.UserDto
import com.example.mall.core.model.LoginDto
import kotlinx.coroutines.flow.Flow

/**
 * 用户相关 Repository 接口
 * 定义在 domain 层，由 data 层实现
 */
interface UserRepository {
    suspend fun login(phone: String, code: String): Result<LoginDto>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<LoginDto>
    fun getUser(): Flow<Result<UserDto>>
    suspend fun updateUser(user: UserDto): Result<UserDto>
    suspend fun isLoggedIn(): Boolean
}

/**
 * 会话列表 Repository 接口
 */
interface ConversationRepository {
    fun getConversations(page: Int, pageSize: Int): Flow<Result<List<com.example.mall.core.model.ConversationDto>>>
    suspend fun pinConversation(id: String): Result<Unit>
    suspend fun unpinConversation(id: String): Result<Unit>
    suspend fun muteConversation(id: String): Result<Unit>
    suspend fun unmuteConversation(id: String): Result<Unit>
    suspend fun deleteConversation(id: String): Result<Unit>
}

/**
 * 消息 Repository 接口
 */
interface MessageRepository {
    fun getMessages(
        conversationId: String,
        page: Int,
        pageSize: Int,
    ): Flow<Result<List<com.example.mall.core.model.MessageDto>>>
    suspend fun sendMessage(conversationId: String, content: String, type: Int): Result<com.example.mall.core.model.MessageDto>
    suspend fun recallMessage(messageId: String): Result<Unit>
    suspend fun deleteMessage(messageId: String): Result<Unit>
}

/**
 * 直播 Repository 接口
 */
interface LiveRepository {
    fun getLiveRooms(page: Int, pageSize: Int): Flow<Result<List<com.example.mall.core.model.LiveRoomDto>>>
    suspend fun getLiveRoomDetail(roomId: String): Result<com.example.mall.core.model.LiveRoomDto>
    suspend fun enterRoom(roomId: String): Result<Unit>
    suspend fun leaveRoom(roomId: String): Result<Unit>
}

/**
 * 语音房 Repository 接口
 */
interface VoiceRoomRepository {
    suspend fun getVoiceRoomDetail(roomId: String): Result<com.example.mall.core.model.VoiceRoomDto>
    suspend fun createRoom(title: String): Result<com.example.mall.core.model.VoiceRoomDto>
    suspend fun joinRoom(roomId: String): Result<com.example.mall.core.model.VoiceRoomDto>
    suspend fun leaveRoom(roomId: String): Result<Unit>
    suspend fun requestSeat(roomId: String, seatIndex: Int): Result<Unit>
    suspend fun leaveSeat(roomId: String): Result<Unit>
    suspend fun inviteSeat(roomId: String, seatIndex: Int, userId: String): Result<Unit>
    suspend fun kickSeat(roomId: String, seatIndex: Int): Result<Unit>
    suspend fun muteSeat(roomId: String, seatIndex: Int): Result<Unit>
    suspend fun unmuteSeat(roomId: String, seatIndex: Int): Result<Unit>
    suspend fun lockSeat(roomId: String, seatIndex: Int): Result<Unit>
    suspend fun unlockSeat(roomId: String, seatIndex: Int): Result<Unit>
}

/**
 * 礼物 Repository 接口
 */
interface GiftRepository {
    suspend fun getGiftList(): Result<List<com.example.mall.core.model.GiftDto>>
    suspend fun sendGift(request: com.example.mall.core.model.GiftSendDto): Result<Unit>
}

/**
 * 钱包 Repository 接口
 */
interface WalletRepository {
    fun getWallet(): Flow<Result<com.example.mall.core.model.WalletDto>>
    fun getTransactions(page: Int, pageSize: Int): Flow<Result<List<com.example.mall.core.model.TransactionDto>>>
    suspend fun recharge(amount: Double): Result<com.example.mall.core.model.TransactionDto>
    suspend fun withdraw(amount: Double): Result<com.example.mall.core.model.TransactionDto>
}

/**
 * 首页 Repository 接口
 */
interface HomeRepository {
    fun getBanners(): Flow<Result<List<com.example.mall.core.model.BannerDto>>>
    fun getTabs(): Flow<Result<List<com.example.mall.core.model.HomeTabDto>>>
    fun getLiveRooms(page: Int, pageSize: Int): Flow<Result<List<com.example.mall.core.model.LiveRoomDto>>>
}
