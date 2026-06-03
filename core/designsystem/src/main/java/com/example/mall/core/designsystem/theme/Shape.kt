package com.example.mall.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 统一形状规范
 */
val MallShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/**
 * 间距规范
 */
object Spacing {
    val spacing2 = 2.dp
    val spacing4 = 4.dp
    val spacing6 = 6.dp
    val spacing8 = 8.dp
    val spacing10 = 10.dp
    val spacing12 = 12.dp
    val spacing16 = 16.dp
    val spacing20 = 20.dp
    val spacing24 = 24.dp
    val spacing28 = 28.dp
    val spacing32 = 32.dp
    val spacing40 = 40.dp
    val spacing48 = 48.dp
    val spacing56 = 56.dp
    val spacing64 = 64.dp
}

/**
 * 尺寸规范
 */
object Dimens {
    // Avatar
    val avatarSmall = 24.dp
    val avatarMedium = 36.dp
    val avatarLarge = 48.dp
    val avatarXLarge = 64.dp
    val avatarXXLarge = 96.dp

    // Icon
    val iconSmall = 16.dp
    val iconMedium = 24.dp
    val iconLarge = 32.dp

    // Button
    val buttonHeight = 48.dp
    val buttonHeightSmall = 36.dp
    val buttonCornerRadius = 24.dp

    // Card
    val cardCornerRadius = 12.dp
    val cardElevation = 2.dp

    // Divider
    val dividerThickness = 0.5.dp

    // Input
    val inputHeight = 48.dp
    val inputCornerRadius = 12.dp

    // Bottom Navigation
    val bottomNavHeight = 56.dp

    // Toolbar
    val toolbarHeight = 56.dp

    // Voice Room Seat
    val voiceSeatSize = 64.dp
    val voiceSeatCornerRadius = 32.dp

    // Gift
    val giftIconSize = 48.dp
    val giftPanelHeight = 280.dp

    // Live
    val liveRoomPreviewHeight = 220.dp
    val liveRoomPreviewCornerRadius = 12.dp
}
