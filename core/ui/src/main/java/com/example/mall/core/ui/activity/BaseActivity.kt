package com.example.mall.core.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.common.theme.ThemeManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * Activity 基类
 *
 * 职责：
 * - 多语言：拦截 attachBaseContext / onConfigurationChanged，确保语言切换全局生效
 * - 主题：监听主题变更，自动刷新 UI
 *
 * 使用方式：所有 Activity 继承此基类（替代直接继承 ComponentActivity）
 *
 * 注意：由于 Hilt @AndroidEntryPoint 不支持继承，
 * 子类需单独标注 @AndroidEntryPoint，但语言/主题逻辑已在此基类统一处理
 */
abstract class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun attachBaseContext(newBase: Context) {
        // 在 attachBaseContext 阶段 Hilt 尚未完成字段注入，需通过 EntryPoint 获取 LocaleManager
        val entryPoint = EntryPointAccessors.fromApplication(
            newBase.applicationContext,
            LocaleManagerEntryPoint::class.java,
        )
        val wrappedContext = entryPoint.localeManager().wrapContext(newBase)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 应用主题配置（在 setContent 之前）
        applyThemeBeforeSetContent()
        super.onCreate(savedInstanceState)

        // 监听语言变化，切换后重建 Activity 使新语言全局生效
        val localeAtCreate = localeManager.currentLocale
        lifecycleScope.launch {
            localeManager.localeChangeFlow.collect { newLocale ->
                if (newLocale != localeAtCreate) {
                    recreate()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // 系统配置变化时（如系统语言切换），重新应用应用内语言设置
        localeManager.applyToConfiguration(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 子类可重写以自定义主题应用逻辑
     * 默认从 ThemeManager 读取当前主题模式并应用
     * Compose 项目通过 MallTheme Composable 处理主题，此处处理 XML 层面的配置
     */
    protected open fun applyThemeBeforeSetContent() {
        // Compose 项目通过 MallTheme Composable 处理主题
        // 此处处理 XML 层面的主题（如 status bar 颜色等）
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LocaleManagerEntryPoint {
        fun localeManager(): LocaleManager
    }
}
