package com.example.mall

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.example.mall.core.common.util.AppLogger

@HiltAndroidApp
class MallApplication : Application() {

    override fun onCreate() {
        super.onCreate()

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
