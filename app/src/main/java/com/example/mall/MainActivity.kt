package com.example.mall

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.common.theme.ThemeManager
import com.example.mall.core.designsystem.theme.MallTheme
import com.example.mall.core.navigation.BottomNavItem
import com.example.mall.core.ui.activity.BaseActivity
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

    MallNavHost(
        navController = navController,
        showBottomBar = showBottomBar,
        localeManager = localeManager,
        themeManager = themeManager,
    )
}
