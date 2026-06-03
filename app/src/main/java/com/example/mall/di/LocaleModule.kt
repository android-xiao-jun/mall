package com.example.mall.di

import android.content.Context
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 多语言管理器 DI 模块
 *
 * 在 app 层组装 LocaleManager，桥接 UserDataStore 持久化能力
 */
@Module
@InstallIn(SingletonComponent::class)
object LocaleModule {

    @Provides
    @Singleton
    fun provideLocaleManager(
        @ApplicationContext appContext: Context,
        userDataStore: UserDataStore,
    ): LocaleManager {
        return LocaleManager(
            context = appContext,
            persistLanguage = { code -> userDataStore.saveLanguage(code) },
            getPersistedLanguageFlow = { userDataStore.getLanguageFlow() },
        )
    }
}
