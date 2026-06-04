package com.example.mall.feature.login.presentation.state

import com.example.mall.core.ui.mvi.UiState

/**
 * 登录弹窗状态
 */
data class LoginUiState(
    val phone: String = "",
    val code: String = "",
    val isSmsCodeSending: Boolean = false,
    val smsCodeCountdown: Int = 0,
    val isLoggingIn: Boolean = false,
    val errorMessage: String? = null,
) : UiState {

    val isPhoneValid: Boolean
        get() = phone.length == 11 && phone.startsWith("1")

    val isCodeValid: Boolean
        get() = code.isNotBlank()

    val canLogin: Boolean
        get() = isPhoneValid && isCodeValid && !isLoggingIn

    val canSendCode: Boolean
        get() = isPhoneValid && !isSmsCodeSending && smsCodeCountdown == 0
}
