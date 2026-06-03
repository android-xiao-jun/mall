package com.example.mall.core.common.auth

/**
 * Token 提供者接口
 *
 * 由 core:datastore 的 UserDataStore 实现
 * 解耦 core:network 对 core:datastore 的直接依赖
 */
interface TokenProvider {
    suspend fun getToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveToken(token: String)
}

/**
 * 登录失效回调接口
 *
 * 由 app 层实现，处理全局退出登录
 */
interface AuthExpiredHandler {
    fun onAuthExpired()
}
