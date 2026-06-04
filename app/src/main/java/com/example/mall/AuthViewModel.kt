package com.example.mall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mall.core.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 全局启动与认证状态 ViewModel
 *
 * 在 app 层观察 UserDataStore 的隐私协议同意状态和登录状态，
 * 控制隐私协议弹窗和登录弹窗的显示与隐藏。
 *
 * 启动流程：隐私协议 → 登录 → 首页
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
) : ViewModel() {

    /**
     * 是否已同意隐私协议，初始值为 false
     */
    val isAgreementAccepted = userDataStore.isAgreementAcceptedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * 是否已登录，初始值为 false
     * DataStore Flow 会自动在登录/登出时更新
     */
    val isLoggedIn = userDataStore.isLoginFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * 用户同意隐私协议
     */
    fun acceptAgreement() {
        viewModelScope.launch {
            userDataStore.setAgreementAccepted(true)
        }
    }

    /**
     * 退出登录
     *
     * 清除用户数据后，isLoggedIn 会自动变为 false，
     * MallApp 中的 LoginDialog 会自动弹出
     */
    fun logout() {
        viewModelScope.launch {
            userDataStore.clearUserData()
        }
    }
}
