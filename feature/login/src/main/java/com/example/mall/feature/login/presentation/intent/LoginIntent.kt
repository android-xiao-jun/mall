package com.example.mall.feature.login.presentation.intent

import com.example.mall.core.ui.mvi.UiIntent

/**
 * 登录页用户行为
 */
sealed class LoginIntent : UiIntent {
    data class PhoneChanged(val phone: String) : LoginIntent()
    data class CodeChanged(val code: String) : LoginIntent()
    data object SendSmsCode : LoginIntent()
    data object Login : LoginIntent()
    data object WechatLogin : LoginIntent()
    data object AppleLogin : LoginIntent()
    data object AgreePolicy : LoginIntent()
    data object DisagreePolicy : LoginIntent()
}
