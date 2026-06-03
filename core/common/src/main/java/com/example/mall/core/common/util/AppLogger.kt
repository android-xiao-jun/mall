package com.example.mall.core.common.util

import timber.log.Timber

/**
 * 统一日志工具，基于 Timber 封装
 *
 * 使用方式：
 * - debug 构建会自动输出到 Logcat
 * - release 构建可通过 [AppTree] 上报到 Crashlytics
 */
object AppLogger {

    fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    fun d(throwable: Throwable, message: String, vararg args: Any?) {
        Timber.d(throwable, message, *args)
    }

    fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    fun e(throwable: Throwable, message: String, vararg args: Any?) {
        Timber.e(throwable, message, *args)
    }

    fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    fun w(throwable: Throwable, message: String, vararg args: Any?) {
        Timber.w(throwable, message, *args)
    }

    fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    fun v(message: String, vararg args: Any?) {
        Timber.v(message, *args)
    }

    /**
     * 在 Release 环境中将 Warning/Error 日志上报到 Crashlytics
     */
    class CrashlyticsTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // 在集成 Firebase Crashlytics 后启用
            // if (priority >= Log.WARN) {
            //     FirebaseCrashlytics.getInstance().recordException(t ?: Exception(message))
            // }
        }
    }
}
