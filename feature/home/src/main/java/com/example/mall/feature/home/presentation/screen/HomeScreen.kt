package com.example.mall.feature.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mall.core.designsystem.theme.MallExtendedTheme
import com.example.mall.feature.home.R
import com.example.mall.feature.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToLive: (String) -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 用户信息卡片
        UserInfoCard(
            isLoggedIn = uiState.isLoggedIn,
            nickname = uiState.nickname,
            phone = uiState.phone,
            userId = uiState.userId,
        )

        Text(
            text = "🎨 Color Palette Preview",
            style = MaterialTheme.typography.headlineMedium,
        )

        MaterialColorSection()
        ExtendedColorSection()
    }
}

// ==================== 用户信息卡片 ====================

@Composable
private fun UserInfoCard(
    isLoggedIn: Boolean,
    nickname: String,
    phone: String,
    userId: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        if (isLoggedIn) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 头像占位
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = nickname,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.home_user_info_format, userId, phone.replaceRange(3, 7, "****")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(28.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.home_not_logged_in),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ==================== Material3 ColorScheme ====================

@Composable
private fun MaterialColorSection() {
    ColorSectionCard(title = "Material3 ColorScheme") {
        val cs = MaterialTheme.colorScheme
        ColorSwatchRow("primary / onPrimary", cs.primary, cs.onPrimary)
        ColorSwatchRow("secondary / onSecondary", cs.secondary, cs.onSecondary)
        ColorSwatchRow("tertiary / onTertiary", cs.tertiary, cs.onTertiary)
        ColorSwatchRow("error / onError", cs.error, cs.onError)
        ColorSwatchRow("background / onBackground", cs.background, cs.onBackground)
        ColorSwatchRow("surface / onSurface", cs.surface, cs.onSurface)
        ColorSwatchRow("surfaceVariant / onSurfaceVariant", cs.surfaceVariant, cs.onSurfaceVariant)
        ColorSwatchRow("outline / outlineVariant", cs.outline, cs.outlineVariant)
    }
}

// ==================== Extended ColorPalette ====================

@Composable
private fun ExtendedColorSection() {
    val ext = MallExtendedTheme.colors

    ColorSectionCard(title = "Extended - 聊天气泡") {
        ColorSwatchRow("chatBubbleSelf / on", ext.chatBubbleSelf, ext.chatOnBubbleSelf)
        ColorSwatchRow("chatBubbleOther / on", ext.chatBubbleOther, ext.chatOnBubbleOther)
        ColorSwatchRow("chatBubbleSystem / on", ext.chatBubbleSystem, ext.chatOnBubbleSystem)
    }

    ColorSectionCard(title = "Extended - 礼物") {
        ColorSwatchRow("giftHighlight / on", ext.giftHighlight, ext.giftOnHighlight)
    }

    ColorSectionCard(title = "Extended - 钱包") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ColorChip("walletPositive", ext.walletPositive, Modifier.weight(1f))
            ColorChip("walletNegative", ext.walletNegative, Modifier.weight(1f))
        }
    }

    ColorSectionCard(title = "Extended - 直播渐变") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(ext.liveGradientStart, ext.liveGradientEnd),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "liveGradientStart → liveGradientEnd",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ==================== 通用组件 ====================

@Composable
private fun ColorSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

/**
 * 颜色色块行：左侧背景色块 + 右侧 on 色文字
 */
@Composable
private fun ColorSwatchRow(
    label: String,
    bgColor: Color,
    onColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Aa",
                color = onColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = colorHex(bgColor),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 单色块芯片
 */
@Composable
private fun ColorChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(
            text = colorHex(color),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun colorHex(color: Color): String {
    val alpha = (color.alpha * 255).toInt()
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()
    return if (alpha == 255) {
        "#%02X%02X%02X".format(red, green, blue)
    } else {
        "#%02X%02X%02X%02X".format(alpha, red, green, blue)
    }
}
