package com.example.mall.core.common.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

/**
 * 主题模式枚举
 */
enum class ThemeMode(
    val code: String,
    val displayName: String,
) {
    /** 跟随系统 */
    SYSTEM("system", "System"),
    /** 浅色模式 */
    LIGHT("light", "Light"),
    /** 深色模式 */
    DARK("dark", "Dark");

    companion object {
        fun fromCode(code: String): ThemeMode {
            return entries.find { it.code == code } ?: SYSTEM
        }
    }
}

/**
 * 主题管理器
 *
 * 职责：
 * - 运行时切换 Dark / Light / System 主题
 * - 主题状态持久化（由外部 UserDataStore 负责）
 * - 提供 Flow 供 Compose UI 观察主题变化
 * - 支持 Material3 / AppCompat 兼容
 *
 * 设计原则：
 * - 不持有 Activity 引用，避免内存泄漏
 * - 持久化委托给外部，通过构造函数注入
 * - 低侵入：Compose 侧通过 [themeModeFlow] 观察即可
 */
class ThemeManager(
    private val persistThemeMode: suspend (String) -> Unit,
    private val getPersistedThemeModeFlow: () -> Flow<String>,
) {

    private val _themeModeFlow = MutableStateFlow(ThemeMode.SYSTEM)
    val themeModeFlow = _themeModeFlow.asStateFlow()

    /** 当前主题模式，内存缓存 */
    val currentThemeMode: ThemeMode
        get() = _themeModeFlow.value

    /**
     * 初始化：从持久化存储读取主题设置
     * 应在 Application.onCreate 中调用
     */
    suspend fun initialize() {
        val code = getPersistedThemeModeFlow().first()
        _themeModeFlow.value = ThemeMode.fromCode(code)
    }

    /**
     * 切换主题模式
     *
     * Compose UI 会通过 [themeModeFlow] 自动响应变化，
     * 不需要手动 recreate Activity
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        _themeModeFlow.value = mode
        persistThemeMode(mode.code)
    }

    /**
     * 判断当前是否为深色模式
     * @param isSystemDarkTheme 系统当前是否为深色模式
     */
    fun isDarkTheme(isSystemDarkTheme: Boolean): Boolean {
        return when (currentThemeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemDarkTheme
        }
    }
}
