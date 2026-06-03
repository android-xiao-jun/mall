package com.example.mall

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.common.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import com.example.mall.core.common.util.AppLogger
import javax.inject.Inject

@HiltAndroidApp
class MallApplication : Application() {

    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        // MultiDex 初始化：minSdk >= 21 时系统自动支持，
        // 但显式调用可确保在低版本设备上兼容
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // 多语言 & 主题初始化：同步读取持久化配置，确保 Activity 创建前完成
        runBlocking {
            localeManager.initialize()
            localeManager.updateApplicationLocale()
            themeManager.initialize()
        }

        initTimber()
        initFirebase()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(AppLogger.CrashlyticsTree())
        }
    }

    private fun initFirebase() {
        // Firebase 初始化是自动的，这里可做额外配置
        // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}
