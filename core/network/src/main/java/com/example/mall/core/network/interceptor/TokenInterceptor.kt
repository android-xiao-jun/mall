package com.example.mall.core.network.interceptor

import com.example.mall.core.common.auth.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Token 拦截器
 *
 * 在请求头中自动添加 Authorization Token
 */
@Singleton
class TokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 不需要 Token 的接口白名单
        val noAuthPaths = listOf(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/sms-code",
            "/api/auth/refresh-token",
        )

        val path = originalRequest.url.encodedPath
        val needsAuth = noAuthPaths.none { path.contains(it) }

        if (!needsAuth) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenProvider.getToken() }

        val request = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
