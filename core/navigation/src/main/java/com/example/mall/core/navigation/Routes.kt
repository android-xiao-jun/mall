package com.example.mall.core.navigation

import kotlinx.serialization.Serializable

// ==================== 顶层路由 ====================

@Serializable
data object LoginRoute

@Serializable
data object HomeRoute

@Serializable
data class ChatRoute(val conversationId: String = "")

@Serializable
data object ConversationRoute

@Serializable
data class VoiceRoomRoute(val roomId: String)

@Serializable
data class LiveRoute(val roomId: String = "")

@Serializable
data class GiftRoute(val targetId: String, val roomId: String = "")

@Serializable
data object WalletRoute

@Serializable
data class ProfileRoute(val userId: String = "")

@Serializable
data object SettingRoute

// ==================== 底部 Tab 路由 ====================

enum class BottomNavItem(
    val route: String,
    val label: String,
) {
    HOME("home", "首页"),
    CONVERSATION("conversation", "消息"),
    LIVE("live_list", "直播"),
    WALLET("wallet", "钱包"),
    PROFILE("profile", "我的"),
}

/**
 * Route 工具方法
 */
object RouteUtils {

    /**
     * 判断是否是底部 Tab 路由
     */
    fun isBottomTabRoute(route: String): Boolean {
        return BottomNavItem.entries.any { it.route == route }
    }

    /**
     * 获取底部 Tab 路由列表
     */
    fun getBottomTabRoutes(): List<String> {
        return BottomNavItem.entries.map { it.route }
    }
}
