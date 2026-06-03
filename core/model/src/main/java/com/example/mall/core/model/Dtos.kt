package com.example.mall.core.model

import kotlinx.serialization.Serializable

// ==================== 用户相关模型 ====================

@Serializable
data class UserDto(
    val uid: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val phone: String = "",
    val email: String = "",
    val gender: Int = 0,
    val birthday: String = "",
    val signature: String = "",
    val level: Int = 1,
    val vipLevel: Int = 0,
    val vipExpireTime: Long = 0,
    val fansCount: Long = 0,
    val followCount: Long = 0,
    val friendCount: Long = 0,
    val coinBalance: Long = 0,
    val diamondBalance: Long = 0,
    val createTime: Long = 0,
)

@Serializable
data class LoginDto(
    val token: String = "",
    val refreshToken: String = "",
    val expireTime: Long = 0,
    val user: UserDto = UserDto(),
)

@Serializable
data class TokenDto(
    val accessToken: String = "",
    val refreshToken: String = "",
    val expireTime: Long = 0,
)

// ==================== IM 相关模型 ====================

@Serializable
data class ConversationDto(
    val id: String = "",
    val type: Int = 0,
    val name: String = "",
    val avatar: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0,
    val unreadCount: Int = 0,
    val memberId: String = "",
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
)

@Serializable
data class MessageDto(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderAvatar: String = "",
    val type: Int = 0,
    val content: String = "",
    val extra: String = "",
    val status: Int = 0,
    val createTime: Long = 0,
)

// ==================== 直播/语音房相关模型 ====================

@Serializable
data class LiveRoomDto(
    val id: String = "",
    val title: String = "",
    val cover: String = "",
    val hostId: String = "",
    val hostName: String = "",
    val hostAvatar: String = "",
    val pullUrl: String = "",
    val pushUrl: String = "",
    val viewerCount: Int = 0,
    val type: Int = 0,
    val status: Int = 0,
    val tags: List<String> = emptyList(),
    val createTime: Long = 0,
)

@Serializable
data class VoiceRoomDto(
    val id: String = "",
    val title: String = "",
    val cover: String = "",
    val hostId: String = "",
    val hostName: String = "",
    val hostAvatar: String = "",
    val memberCount: Int = 0,
    val maxMemberCount: Int = 8,
    val status: Int = 0,
    val seats: List<VoiceSeatDto> = emptyList(),
    val createTime: Long = 0,
)

@Serializable
data class VoiceSeatDto(
    val index: Int = 0,
    val userId: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
)

// ==================== 礼物相关模型 ====================

@Serializable
data class GiftDto(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val animation: String = "",
    val price: Long = 0,
    val type: Int = 0,
    val category: String = "",
    val isCombo: Boolean = false,
)

@Serializable
data class GiftSendDto(
    val giftId: String = "",
    val targetId: String = "",
    val count: Int = 1,
    val roomId: String = "",
)

// ==================== 钱包相关模型 ====================

@Serializable
data class WalletDto(
    val balance: Double = 0.0,
    val frozenAmount: Double = 0.0,
    val coinBalance: Long = 0,
    val diamondBalance: Long = 0,
)

@Serializable
data class TransactionDto(
    val id: String = "",
    val type: Int = 0,
    val amount: Double = 0.0,
    val status: Int = 0,
    val description: String = "",
    val createTime: Long = 0,
)

@Serializable
data class RechargeDto(
    val id: String = "",
    val amount: Double = 0.0,
    val coinAmount: Long = 0,
    val bonusAmount: Long = 0,
    val currency: String = "CNY",
)

// ==================== 首页相关模型 ====================

@Serializable
data class BannerDto(
    val id: String = "",
    val title: String = "",
    val image: String = "",
    val link: String = "",
    val type: Int = 0,
    val sort: Int = 0,
)

@Serializable
data class HomeTabDto(
    val id: String = "",
    val name: String = "",
    val type: Int = 0,
    val icon: String = "",
    val sort: Int = 0,
)
