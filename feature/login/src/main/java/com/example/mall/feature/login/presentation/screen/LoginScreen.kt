package com.example.mall.feature.login.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mall.feature.login.presentation.effect.LoginEffect
import com.example.mall.feature.login.presentation.intent.LoginIntent
import com.example.mall.feature.login.presentation.viewmodel.LoginViewModel
import android.widget.Toast

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onLoginSuccess()
                is LoginEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is LoginEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
                is LoginEffect.StartSmsCountdown -> {
                    // 倒计时由 ViewModel 管理
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Logo / App Name
        Text(
            text = "Mall",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Phone Input
        OutlinedTextField(
            value = state.phone,
            onValueChange = { viewModel.sendIntent(LoginIntent.PhoneChanged(it)) },
            label = { Text("手机号") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SMS Code Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.code,
                onValueChange = { viewModel.sendIntent(LoginIntent.CodeChanged(it)) },
                label = { Text("验证码") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextButton(
                onClick = { viewModel.sendIntent(LoginIntent.SendSmsCode) },
                enabled = state.canSendCode,
            ) {
                if (state.isSmsCodeSending) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp,
                    )
                } else if (state.smsCodeCountdown > 0) {
                    Text("${state.smsCodeCountdown}s")
                } else {
                    Text("获取验证码")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Policy Agreement
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = state.isPolicyAgreed,
                onCheckedChange = { agreed ->
                    viewModel.sendIntent(
                        if (agreed) LoginIntent.AgreePolicy else LoginIntent.DisagreePolicy,
                    )
                },
            )
            Text(
                text = "我已阅读并同意",
                style = MaterialTheme.typography.bodySmall,
            )
            TextButton(onClick = { /* 查看用户协议 */ }) {
                Text("用户协议")
            }
            Text(
                text = "和",
                style = MaterialTheme.typography.bodySmall,
            )
            TextButton(onClick = { /* 查看隐私政策 */ }) {
                Text("隐私政策")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = { viewModel.sendIntent(LoginIntent.Login) },
            enabled = state.canLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            if (state.isLoggingIn) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text("登录")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Third-party login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            OutlinedButton(onClick = { viewModel.sendIntent(LoginIntent.WechatLogin) }) {
                Text("微信登录")
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = { viewModel.sendIntent(LoginIntent.AppleLogin) }) {
                Text("Apple 登录")
            }
        }
    }
}
