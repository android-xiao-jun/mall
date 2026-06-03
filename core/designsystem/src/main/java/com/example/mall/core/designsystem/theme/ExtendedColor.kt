package com.example.mall.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Mall 扩展颜色调色板
 *
 * 用于存放 Material3 ColorScheme 未覆盖的业务专属颜色，
 * 如聊天气泡、礼物高亮、钱包正负值等。
 *
 * 设计原则：
 * - 每个颜色成对出现（背景色 + 其上的前景色），命名遵循 `xxx` / `onXxx` 规范
 * - Light / Dark 各维护一份，通过 CompositionLocal 自动切换
 * - 新增业务颜色只需在此数据类中加字段，并在 Light/Dark 实例中赋值
 *
 * 使用方式：
 * ```kotlin
 * val colors = MallExtendedTheme.colors
 * Box(color = colors.chatBubbleSelf) {
 *     Text("Hello", color = colors.chatOnBubbleSelf)
 * }
 * ```
 */
@Immutable
data class ExtendedColorPalette(
    // ==================== 聊天气泡 ====================
    /** 自己发送的消息气泡背景色 */
    val chatBubbleSelf: Color,
    /** 自己气泡上的文字/图标色 */
    val chatOnBubbleSelf: Color,
    /** 对方发送的消息气泡背景色 */
    val chatBubbleOther: Color,
    /** 对方气泡上的文字/图标色 */
    val chatOnBubbleOther: Color,
    /** 系统消息气泡背景色 */
    val chatBubbleSystem: Color,
    /** 系统消息文字色 */
    val chatOnBubbleSystem: Color,

    // ==================== 礼物 ====================
    /** 礼物高亮背景色 */
    val giftHighlight: Color,
    /** 礼物高亮上的文字色 */
    val giftOnHighlight: Color,

    // ==================== 钱包 ====================
    /** 收入/正值颜色 */
    val walletPositive: Color,
    /** 支出/负值颜色 */
    val walletNegative: Color,

    // ==================== 直播 ====================
    /** 直播渐变起始色 */
    val liveGradientStart: Color,
    /** 直播渐变结束色 */
    val liveGradientEnd: Color,
)

/**
 * Light 模式扩展调色板
 */
val LightExtendedColorPalette = ExtendedColorPalette(
    chatBubbleSelf = ChatBubbleSelf,
    chatOnBubbleSelf = ChatOnBubbleSelf,
    chatBubbleOther = ChatBubbleOther,
    chatOnBubbleOther = ChatOnBubbleOther,
    chatBubbleSystem = ChatBubbleSystem,
    chatOnBubbleSystem = ChatOnBubbleSystem,
    giftHighlight = GiftHighlight,
    giftOnHighlight = GiftOnHighlight,
    walletPositive = WalletPositive,
    walletNegative = WalletNegative,
    liveGradientStart = LiveGradientStart,
    liveGradientEnd = LiveGradientEnd,
)

/**
 * Dark 模式扩展调色板
 */
val DarkExtendedColorPalette = ExtendedColorPalette(
    chatBubbleSelf = DarkChatBubbleSelf,
    chatOnBubbleSelf = DarkChatOnBubbleSelf,
    chatBubbleOther = DarkChatBubbleOther,
    chatOnBubbleOther = DarkChatOnBubbleOther,
    chatBubbleSystem = DarkChatBubbleSystem,
    chatOnBubbleSystem = DarkChatOnBubbleSystem,
    giftHighlight = DarkGiftHighlight,
    giftOnHighlight = DarkGiftOnHighlight,
    walletPositive = DarkWalletPositive,
    walletNegative = DarkWalletNegative,
    liveGradientStart = DarkLiveGradientStart,
    liveGradientEnd = DarkLiveGradientEnd,
)

/**
 * 扩展颜色的 CompositionLocal
 *
 * 由 MallTheme 在顶层提供，子树中通过 MallExtendedTheme.colors 访问
 */
val LocalExtendedColorPalette = staticCompositionLocalOf { LightExtendedColorPalette }

/**
 * 扩展主题访问入口
 *
 * 使用方式与 MaterialTheme 类似：
 * ```kotlin
 * // 读取扩展颜色
 * MallExtendedTheme.colors.chatBubbleSelf
 *
 * // 与 MaterialTheme 搭配使用
 * Surface(color = MaterialTheme.colorScheme.surface) {
 *     Text(
 *         text = "Hello",
 *         color = MallExtendedTheme.colors.chatOnBubbleSelf
 *     )
 * }
 * ```
 */
object MallExtendedTheme {
    /** 当前主题下的扩展颜色调色板 */
    val colors: ExtendedColorPalette
        @Composable get() = LocalExtendedColorPalette.current
}
