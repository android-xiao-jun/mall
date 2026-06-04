package com.example.mall.feature.login.presentation.effect

import com.example.mall.core.ui.mvi.UiEffect

/**
 * 登录弹窗一次性事件
 */
sealed class LoginEffect : UiEffect {
    data object LoginSuccess : LoginEffect()
    data class ShowToast(val message: String) : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}
