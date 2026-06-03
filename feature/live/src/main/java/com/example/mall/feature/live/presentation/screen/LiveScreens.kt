package com.example.mall.feature.live.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.mall.feature.live.R

@Composable
fun LiveListScreen(
    onNavigateToLiveRoom: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(R.string.nav_live))
    }
}

@Composable
fun LiveRoomScreen(
    roomId: String,
    onBack: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "直播间: $roomId")
    }
}
