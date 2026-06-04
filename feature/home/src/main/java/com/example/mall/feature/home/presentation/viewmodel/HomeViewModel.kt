package com.example.mall.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mall.core.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 首页 ViewModel
 *
 * 观察 UserDataStore 中的登录状态和用户信息，
 * 供 HomeScreen 显示用户信息卡片。
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    userDataStore: UserDataStore,
) : ViewModel() {

    /**
     * 首页用户信息状态
     */
    val uiState = combine(
        userDataStore.isLoginFlow(),
        userDataStore.getUserIdFlow(),
        userDataStore.getUserNicknameFlow(),
        userDataStore.getUserAvatarFlow(),
        userDataStore.getUserPhoneFlow(),
    ) { isLogin, userId, nickname, avatar, phone ->
        HomeUiState(
            isLoggedIn = isLogin,
            userId = userId ?: "",
            nickname = nickname ?: "",
            avatar = avatar ?: "",
            phone = phone ?: "",
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )
}

/**
 * 首页用户信息状态
 */
data class HomeUiState(
    val isLoggedIn: Boolean = false,
    val userId: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val phone: String = "",
)
