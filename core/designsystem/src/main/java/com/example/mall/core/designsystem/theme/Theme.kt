package com.example.mall.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.mall.core.common.theme.ThemeManager
import com.example.mall.core.common.theme.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    error = ErrorColor,
    onError = OnError,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = Neutral30,
    outline = Neutral50,
    outlineVariant = Neutral60,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    tertiary = Tertiary,
    onTertiary = Color.White,
    error = DarkError,
    onError = DarkOnError,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral60,
    outline = Neutral50,
    outlineVariant = Neutral40,
)

/**
 * Mall 主题 Composable
 *
 * 支持：
 * - 动态颜色（Android 12+ Material You）
 * - Dark / Light / System 主题切换
 * - 通过 ThemeManager 运行时切换，无需 recreate
 * - 扩展颜色调色板（通过 MallExtendedTheme.colors 访问）
 */
@Composable
fun MallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = when {
        darkTheme -> DarkExtendedColorPalette
        else -> LightExtendedColorPalette
    }

    // 同步状态栏 / 导航栏图标明暗：
    // Light 主题 → 深色图标（isAppearanceLight = true）
    // Dark 主题 → 浅色图标（isAppearanceLight = false）
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtendedColorPalette provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MallTypography,
            shapes = MallShapes,
            content = content,
        )
    }
}

/**
 * 响应式 Mall 主题 Composable
 *
 * 自动订阅 ThemeManager 的主题模式变化，
 * 切换主题时无需手动 recreate Activity。
 *
 * 使用方式：
 * ```kotlin
 * val themeManager: ThemeManager = ... // Hilt 注入
 * MallTheme(themeManager = themeManager) {
 *     // content
 * }
 * ```
 */
@Composable
fun MallTheme(
    themeManager: ThemeManager,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val themeMode by themeManager.themeModeFlow.collectAsState()
    val isSystemDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemDarkTheme
    }

    MallTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content,
    )
}
