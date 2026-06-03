package com.example.mall.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mall.core.ui.R

/**
 * 全屏 Loading 状态
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message ?: stringResource(R.string.state_loading),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 全屏错误状态
 */
@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message ?: stringResource(R.string.state_load_failed),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.state_retry))
            }
        }
    }
}

/**
 * 全屏空状态
 */
@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message ?: stringResource(R.string.state_no_data),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
