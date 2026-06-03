package com.example.mall.feature.setting.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mall.core.common.i18n.AppLanguage
import com.example.mall.core.common.i18n.LocaleManager
import com.example.mall.core.common.theme.ThemeManager
import com.example.mall.core.common.theme.ThemeMode
import com.example.mall.feature.setting.R
import kotlinx.coroutines.launch

/**
 * 设置页面
 *
 * 集成语言切换和主题切换功能
 *
 * @param localeManager 多语言管理器（由 Hilt 注入）
 * @param themeManager 主题管理器（由 Hilt 注入）
 */
@Composable
fun SettingScreen(
    localeManager: LocaleManager,
    themeManager: ThemeManager,
    onBack: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val currentThemeMode by themeManager.themeModeFlow.collectAsState()
    val currentLanguage by localeManager.currentLanguageFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 标题
        Text(
            text = stringResource(R.string.setting_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        // 主题设置卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.setting_theme),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ThemeMode.entries.forEach { mode ->
                        FilterChip(
                            selected = currentThemeMode == mode,
                            onClick = {
                                coroutineScope.launch {
                                    themeManager.setThemeMode(mode)
                                }
                            },
                            label = {
                                Text(
                                    stringResource(
                                        when (mode) {
                                            ThemeMode.SYSTEM -> R.string.setting_theme_system
                                            ThemeMode.LIGHT -> R.string.setting_theme_light
                                            ThemeMode.DARK -> R.string.setting_theme_dark
                                        }
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }

        // 语言设置卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.setting_language),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AppLanguage.entries.forEach { language ->
                        FilterChip(
                            selected = currentLanguage == language,
                            onClick = {
                                coroutineScope.launch {
                                    localeManager.setLanguage(language)
                                }
                            },
                            label = {
                                val displayName = if (language == AppLanguage.SYSTEM) {
                                    stringResource(R.string.setting_language_system)
                                } else {
                                    language.displayName
                                }
                                Text("$displayName (${language.code})")
                            },
                        )
                    }
                }
            }
        }
    }
}
