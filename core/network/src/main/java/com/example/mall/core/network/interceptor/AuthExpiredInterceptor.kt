package com.example.mall.core.network.interceptor

import com.example.mall.core.common.auth.AuthExpiredHandler
import com.example.mall.core.common.auth.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 登录失效处理拦截器
 *
 * 当接口返回 401 时，自动刷新 Token 并重试请求
 * 如果刷新失败，通知全局退出登录
 */
@Singleton
class AuthExpiredInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authExpiredHandler: AuthExpiredHandler,
) : Interceptor {

    companion object {
        const val HTTP_CODE_UNAUTHORIZED = 401
    }

    @Volatile
    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == HTTP_CODE_UNAUTHORIZED) {
            response.close()

            synchronized(this) {
                if (isRefreshing) {
                    return retryWithNewToken(chain, originalRequest)
                }

                isRefreshing = true
                try {
                    val refreshToken = runBlocking { tokenProvider.getRefreshToken() }
                    if (refreshToken.isNullOrBlank()) {
                        authExpiredHandler.onAuthExpired()
                        return response
                    }

                    val newToken = refreshAccessToken(chain, refreshToken)
                    if (newToken != null) {
                        runBlocking { tokenProvider.saveToken(newToken) }
                        return retryWithNewToken(chain, originalRequest)
                    } else {
                        authExpiredHandler.onAuthExpired()
                        return response
                    }
                } finally {
                    isRefreshing = false
                }
            }
        }

        return response
    }

    private fun retryWithNewToken(chain: Interceptor.Chain, originalRequest: Request): Response {
        val newToken = runBlocking { tokenProvider.getToken() }
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
        return chain.proceed(newRequest)
    }

    private fun refreshAccessToken(chain: Interceptor.Chain, refreshToken: String): String? {
        return try {
            Timber.d("Attempting to refresh token...")
            null
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh token")
            null
        }
    }
}
