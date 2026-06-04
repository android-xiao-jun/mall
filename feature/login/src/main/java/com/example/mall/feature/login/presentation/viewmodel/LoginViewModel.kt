package com.example.mall.feature.login.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import android.content.Context
import com.example.mall.core.common.dispatcher.IoDispatcher
import com.example.mall.core.common.result.Result
import com.example.mall.core.datastore.UserDataStore
import com.example.mall.core.domain.repository.UserRepository
import com.example.mall.core.ui.mvi.BaseViewModel
import com.example.mall.feature.login.presentation.effect.LoginEffect
import com.example.mall.feature.login.presentation.intent.LoginIntent
import com.example.mall.feature.login.presentation.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import com.example.mall.feature.login.R

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
) : BaseViewModel<LoginIntent, LoginUiState, LoginEffect>() {

    override fun createInitialState() = LoginUiState()

    init {
        handleIntent { intent ->
            when (intent) {
                is LoginIntent.PhoneChanged -> handlePhoneChanged(intent.phone)
                is LoginIntent.CodeChanged -> handleCodeChanged(intent.code)
                is LoginIntent.SendSmsCode -> handleSendSmsCode()
                is LoginIntent.Login -> handleLogin()
            }
        }
    }

    private fun handlePhoneChanged(phone: String) {
        // 限制只能输入数字，最长11位
        val filtered = phone.filter { it.isDigit() }.take(11)
        setState { copy(phone = filtered, errorMessage = null) }
    }

    private fun handleCodeChanged(code: String) {
        // 限制只能输入数字
        val filtered = code.filter { it.isDigit() }
        setState { copy(code = filtered, errorMessage = null) }
    }

    private suspend fun handleSendSmsCode() {
        if (!currentState.canSendCode) return

        setState { copy(isSmsCodeSending = true) }

        // 模拟发送验证码
        withContext(ioDispatcher) {
            delay(500)
        }

        setState { copy(isSmsCodeSending = false) }
        sendEffect(LoginEffect.ShowToast(context.getString(R.string.login_code_sent)))

        // 启动60秒倒计时
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
                // 保存 Token 和用户信息到 DataStore
                withContext(ioDispatcher) {
                    userDataStore.saveToken(result.data.token)
                    userDataStore.saveRefreshToken(result.data.refreshToken)
                    userDataStore.saveTokenExpireTime(result.data.expireTime)
                    // 保存用户信息（包含 isLogin = true）
                    userDataStore.saveUserInfo(
                        userId = result.data.user.uid,
                        nickname = result.data.user.nickname,
                        avatar = result.data.user.avatar,
                        phone = result.data.user.phone,
                    )
                }

                setState { copy(isLoggingIn = false) }
                sendEffect(LoginEffect.LoginSuccess)
            }
            is Result.Error -> {
                setState { copy(isLoggingIn = false, errorMessage = result.exception.message) }
                sendEffect(LoginEffect.ShowError(result.exception.message ?: context.getString(R.string.login_failed)))
            }
            is Result.Loading -> {
                // 不应该到这里
            }
        }
    }
}
