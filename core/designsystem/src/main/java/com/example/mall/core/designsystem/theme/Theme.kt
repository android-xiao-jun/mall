package com.example.mall.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
 */
@Composable
fun MallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                androidx.compose.material3.dynamicDarkColorScheme(context)
            } else {
                androidx.compose.material3.dynamicLightColorScheme(context)
            }
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MallTypography,
        shapes = MallShapes,
        content = content,
    )
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
