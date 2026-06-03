package com.example.mall.feature.login.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.mall.core.common.dispatcher.IoDispatcher
import com.example.mall.core.common.result.Result
import com.example.mall.core.datastore.UserDataStore
import com.example.mall.core.domain.repository.UserRepository
import com.example.mall.core.ui.mvi.BaseViewModel
import com.example.mall.feature.login.presentation.effect.LoginEffect
import com.example.mall.feature.login.presentation.intent.LoginIntent
import com.example.mall.feature.login.presentation.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<LoginIntent, LoginUiState, LoginEffect>() {

    override fun createInitialState() = LoginUiState()

    init {
        handleIntent { intent ->
            when (intent) {
                is LoginIntent.PhoneChanged -> handlePhoneChanged(intent.phone)
                is LoginIntent.CodeChanged -> handleCodeChanged(intent.code)
                is LoginIntent.SendSmsCode -> handleSendSmsCode()
                is LoginIntent.Login -> handleLogin()
                is LoginIntent.WechatLogin -> handleWechatLogin()
                is LoginIntent.AppleLogin -> handleAppleLogin()
                is LoginIntent.AgreePolicy -> handleAgreePolicy(true)
                is LoginIntent.DisagreePolicy -> handleAgreePolicy(false)
            }
        }
    }

    private fun handlePhoneChanged(phone: String) {
        setState { copy(phone = phone, errorMessage = null) }
    }

    private fun handleCodeChanged(code: String) {
        setState { copy(code = code, errorMessage = null) }
    }

    private fun handleAgreePolicy(agreed: Boolean) {
        setState { copy(isPolicyAgreed = agreed) }
    }

    private suspend fun handleSendSmsCode() {
        if (!currentState.canSendCode) return

        setState { copy(isSmsCodeSending = true) }

        // 模拟发送验证码
        withContext(ioDispatcher) {
            delay(1000)
        }

        setState { copy(isSmsCodeSending = false) }
        sendEffect(LoginEffect.StartSmsCountdown)
        sendEffect(LoginEffect.ShowToast("验证码已发送"))

        // 启动倒计时
        startCountdown()
    }

    private suspend fun startCountdown() {
        for (i in 60 downTo 1) {
            setState { copy(smsCodeCountdown = i) }
            delay(1000)
        }
        setState { copy(smsCodeCountdown = 0) }
    }

    private suspend fun handleLogin() {
        if (!currentState.canLogin) return

        setState { copy(isLoggingIn = true, errorMessage = null) }

        when (val result = userRepository.login(currentState.phone, currentState.code)) {
            is Result.Success -> {
                // 保存 Token 和用户信息
                withContext(ioDispatcher) {
                    userDataStore.saveToken(result.data.token)
                    userDataStore.saveRefreshToken(result.data.refreshToken)
                    userDataStore.saveTokenExpireTime(result.data.expireTime)
                    userDataStore.saveUserId(result.data.user.uid)
                    userDataStore.saveUserNickname(result.data.user.nickname)
                    userDataStore.saveUserAvatar(result.data.user.avatar)
                }

                setState { copy(isLoggingIn = false) }
                sendEffect(LoginEffect.NavigateToHome)
            }
            is Result.Error -> {
                setState { copy(isLoggingIn = false, errorMessage = result.exception.message) }
                sendEffect(LoginEffect.ShowError(result.exception.message ?: "登录失败"))
            }
            is Result.Loading -> {
                // 不应该到这里
            }
        }
    }

    private suspend fun handleWechatLogin() {
        Timber.d("Wechat login")
        // 实际接入微信 SDK
        sendEffect(LoginEffect.ShowToast("微信登录开发中"))
    }

    private suspend fun handleAppleLogin() {
        Timber.d("Apple login")
        // 实际接入 Apple Sign In
        sendEffect(LoginEffect.ShowToast("Apple 登录开发中"))
    }
}
