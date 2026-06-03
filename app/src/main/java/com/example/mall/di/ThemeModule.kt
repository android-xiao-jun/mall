package com.example.mall.di

import com.example.mall.core.common.theme.ThemeManager
import com.example.mall.core.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 主题管理器 DI 模块
 *
 * 在 app 层组装 ThemeManager，桥接 UserDataStore 持久化能力
 */
@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemeManager(
        userDataStore: UserDataStore,
    ): ThemeManager {
        return ThemeManager(
            persistThemeMode = { code -> userDataStore.saveThemeMode(code) },
            getPersistedThemeModeFlow = { userDataStore.getThemeModeFlow() },
        )
    }
}
