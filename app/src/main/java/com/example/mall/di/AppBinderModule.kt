package com.example.mall.di

import com.example.mall.core.datastore.UserDataStore
import com.example.mall.core.common.auth.AuthExpiredHandler
import com.example.mall.core.common.auth.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 将 UserDataStore 绑定为 TokenProvider 的实现
 * 将 AuthExpiredHandlerImpl 绑定为 AuthExpiredHandler 的实现
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBinderModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: UserDataStore): TokenProvider

    @Binds
    @Singleton
    abstract fun bindAuthExpiredHandler(impl: AuthExpiredHandlerImpl): AuthExpiredHandler
}

/**
 * 登录失效处理实现
 */
@Singleton
class AuthExpiredHandlerImpl @Inject constructor() : AuthExpiredHandler {
    override fun onAuthExpired() {
        Timber.w("Token expired, need to logout")
        // 实际项目中应通过 SharedFlow 通知 UI 层跳转登录页
    }
}
