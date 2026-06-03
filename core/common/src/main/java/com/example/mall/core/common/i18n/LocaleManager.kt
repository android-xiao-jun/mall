package com.example.mall.core.common.i18n

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.util.Locale

/**
 * 支持的语言枚举
 *
 * 包含系统语言 + 自定义语言扩展
 */
enum class AppLanguage(
    val code: String,
    val displayName: String,
    val locale: Locale,
) {
    /** 跟随系统语言 */
    SYSTEM("system", "System", Locale.getDefault()),
    /** 简体中文 */
    ZH("zh", "简体中文", Locale.SIMPLIFIED_CHINESE),
    /** English */
    EN("en", "English", Locale.ENGLISH),
    /** Español */
    ES("es", "Español", Locale("es")),
    /** العربية (RTL) */
    AR("ar", "العربية", Locale("ar")),
    /** Русский */
    RU("ru", "Русский", Locale("ru")),
    /** Қазақ */
    KK("kk", "Қазақ", Locale("kk")),
    /** ئۇيغۇرچە (RTL) */
    UG("ug", "ئۇيغۇرچە", Locale("ug"));

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: SYSTEM
        }
    }
}

/**
 * 多语言管理器
 *
 * 职责：
 * - 运行时动态切换语言（无需重启 Activity）
 * - 兼容 Android 7 ~ Android 14
 * - 语言配置持久化（由外部 UserDataStore 负责）
 * - 提供 Context 包装，使 Activity/Fragment/Dialog 全局生效
 *
 * 设计原则：
 * - 单一职责：只负责 Locale 的计算与 Context 包装
 * - 持久化委托给 UserDataStore，通过构造函数注入
 * - 低侵入：业务代码只需调用 [wrapContext] 和 [applyToConfiguration]
 */
class LocaleManager(
    private val context: Context,
    private val persistLanguage: suspend (String) -> Unit,
    private val getPersistedLanguageFlow: () -> Flow<String>,
) {

    private val _localeChangeFlow = MutableSharedFlow<Locale>(extraBufferCapacity = 1)
    val localeChangeFlow: Flow<Locale> = _localeChangeFlow.asSharedFlow()

    private val _currentLanguageFlow = MutableStateFlow(AppLanguage.SYSTEM)
    val currentLanguageFlow: StateFlow<AppLanguage> = _currentLanguageFlow.asStateFlow()

    /** 当前应用语言，内存缓存 */
    @Volatile
    private var currentLanguage: AppLanguage = AppLanguage.SYSTEM

    /** 当前生效的 Locale（如果是 SYSTEM 则返回系统实际 Locale） */
    val currentLocale: Locale
        get() = getEffectiveLocale(currentLanguage)

    /**
     * 初始化：从持久化存储读取语言设置
     * 应在 Application.onCreate 中调用
     */
    suspend fun initialize() {
        val lang = getPersistedLanguageFlow().first()
        currentLanguage = AppLanguage.fromCode(lang)
        _currentLanguageFlow.value = currentLanguage
    }

    /**
     * 切换语言
     *
     * @param language 目标语言
     * @return 是否需要重启 Activity（仅当系统语言与目标不同时返回 true）
     */
    suspend fun setLanguage(language: AppLanguage) {
        val oldLocale = currentLocale
        currentLanguage = language
        _currentLanguageFlow.value = language
        val newLocale = currentLocale

        // 持久化
        persistLanguage(language.code)

        // 通知观察者
        if (oldLocale != newLocale) {
            _localeChangeFlow.tryEmit(newLocale)
        }
    }

    /**
     * 获取当前语言枚举
     */
    fun getCurrentLanguage(): AppLanguage = _currentLanguageFlow.value

    /**
     * 包装 Context，使指定语言的资源生效
     *
     * 用于：
     * - Activity.attachBaseContext
     * - Context.getApplicationContext 的包装
     * - Service / BroadcastReceiver 的 Context 包装
     */
    fun wrapContext(baseContext: Context): Context {
        val locale = currentLocale
        val config = Configuration(baseContext.resources.configuration)
        applyLocaleToConfiguration(config, locale)
        return baseContext.createConfigurationContext(config)
    }

    /**
     * 将当前语言应用到 Configuration
     *
     * 用于 Activity.onConfigurationChanged 中更新资源
     */
    fun applyToConfiguration(config: Configuration) {
        applyLocaleToConfiguration(config, currentLocale)
    }

    /**
     * 更新 Application Context 的 Locale
     * 在语言切换后调用，确保非 Activity 场景（Toast、Notification 等）也能使用正确语言
     */
    fun updateApplicationLocale() {
        val locale = currentLocale
        val config = Configuration(context.resources.configuration)
        applyLocaleToConfiguration(config, locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    // ==================== Internal ====================

    private fun getEffectiveLocale(language: AppLanguage): Locale {
        return if (language == AppLanguage.SYSTEM) {
            getSystemLocale()
        } else {
            language.locale
        }
    }

    private fun getSystemLocale(): Locale {
        val config = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            @Suppress("DEPRECATION")
            config.locale
        }
    }

    private fun applyLocaleToConfiguration(config: Configuration, locale: Locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else {
            @Suppress("DEPRECATION")
            config.setLocale(locale)
        }
        Locale.setDefault(locale)
    }
}
