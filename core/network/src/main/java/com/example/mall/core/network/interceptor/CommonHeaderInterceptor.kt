package com.example.mall.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 公共 Header 拦截器
 *
 * 统一添加平台、版本号、设备信息等公共 Header
 */
@Singleton
class CommonHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val request = originalRequest.newBuilder().apply {
            header("Platform", "Android")
            header("App-Version", getAppVersion())
            header("Device-Id", getDeviceId())
            header("OS-Version", android.os.Build.VERSION.RELEASE)
            header("Device-Model", android.os.Build.MODEL)
            header("Accept-Language", getLanguage())
        }.build()

        return chain.proceed(request)
    }

    private fun getAppVersion(): String {
        // 在实际项目中从 PackageInfo 获取
        return "1.0.0"
    }

    private fun getDeviceId(): String {
        // 在实际项目中从 DataStore 或 Settings.Secure 获取
        return ""
    }

    private fun getLanguage(): String {
        return java.util.Locale.getDefault().language
    }
}
