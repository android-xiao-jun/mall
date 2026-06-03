package com.example.mall.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mall.core.navigation.BottomNavItem
import com.example.mall.feature.home.presentation.screen.HomeScreen
import com.example.mall.feature.conversation.presentation.screen.ConversationScreen
import com.example.mall.feature.live.presentation.screen.LiveListScreen
import com.example.mall.feature.wallet.presentation.screen.WalletScreen
import com.example.mall.feature.profile.presentation.screen.ProfileScreen

private const val ANIM_DURATION = 300

@Composable
fun MallNavHost(
    navController: NavHostController,
    showBottomBar: Boolean,
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MallBottomBar(navController = navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.HOME.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(ANIM_DURATION),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(ANIM_DURATION),
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(ANIM_DURATION),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(ANIM_DURATION),
                )
            },
        ) {
            composable(route = BottomNavItem.HOME.route) {
                HomeScreen()
            }

            composable(route = BottomNavItem.CONVERSATION.route) {
                ConversationScreen()
            }

            composable(route = BottomNavItem.LIVE.route) {
                LiveListScreen()
            }

            composable(route = BottomNavItem.WALLET.route) {
                WalletScreen()
            }

            composable(route = BottomNavItem.PROFILE.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
private fun MallBottomBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        BottomNavItem.entries.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(text = item.label) },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

private val BottomNavItem.icon: ImageVector
    get() = when (this) {
        BottomNavItem.HOME -> Icons.Filled.Home
        BottomNavItem.CONVERSATION -> Icons.Filled.Chat
        BottomNavItem.LIVE -> Icons.Filled.LiveTv
        BottomNavItem.WALLET -> Icons.Filled.AccountBalanceWallet
        BottomNavItem.PROFILE -> Icons.Filled.Person
    }
