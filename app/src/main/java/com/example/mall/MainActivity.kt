package com.example.mall

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.common.theme.ThemeManager
import com.example.mall.core.designsystem.theme.MallTheme
import com.example.mall.core.navigation.BottomNavItem
import com.example.mall.core.ui.activity.BaseActivity
import com.example.mall.feature.login.presentation.screen.LoginDialog
import com.example.mall.navigation.MallNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MallTheme(themeManager = themeManager) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MallApp(
                        localeManager = localeManager,
                        themeManager = themeManager,
                    )
                }
            }
        }
    }
}

@Composable
fun MallApp(
    localeManager: LocaleManager,
    themeManager: ThemeManager,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in BottomNavItem.entries.map { it.route }

    val authViewModel: AuthViewModel = hiltViewModel()
    val isAgreementAccepted by authViewModel.isAgreementAccepted.collectAsStateWithLifecycle()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLoginDialog by remember { mutableStateOf(false) }

    MallNavHost(
        navController = navController,
        showBottomBar = showBottomBar,
        localeManager = localeManager,
        themeManager = themeManager,
        isLoggedIn = isLoggedIn,
        onLogout = { authViewModel.logout() },
        onRequireLogin = { showLoginDialog = true },
    )

    // 启动流程：隐私协议 → 首页（可免登录浏览）
    // 未同意隐私协议时弹出隐私协议弹窗
    if (!isAgreementAccepted) {
        PrivacyAgreementDialog(
            onAgree = { authViewModel.acceptAgreement() },
            onDisagree = {
                // 用户拒绝，退出 App
                (context as? androidx.activity.ComponentActivity)?.finishAffinity()
            },
        )
    } else if (showLoginDialog && !isLoggedIn) {
        // 按需弹出登录弹窗，可关闭
        LoginDialog(
            onLoginSuccess = { showLoginDialog = false },
            onDismiss = { showLoginDialog = false },
        )
    }
}
