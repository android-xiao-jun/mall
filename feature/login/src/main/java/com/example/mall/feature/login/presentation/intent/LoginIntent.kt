package com.example.mall.feature.login.presentation.intent

import com.example.mall.core.ui.mvi.UiIntent

/**
 * 登录弹窗用户行为
 */
sealed class LoginIntent : UiIntent {
    data class PhoneChanged(val phone: String) : LoginIntent()
    data class CodeChanged(val code: String) : LoginIntent()
    data object SendSmsCode : LoginIntent()
    data object Login : LoginIntent()
}
