package com.example.mall

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * 隐私协议弹窗
 *
 * App 首次启动时弹出，用户必须同意才能继续使用。
 * 不可通过点击外部关闭。
 *
 * @param onAgree 用户点击"同意"
 * @param onDisagree 用户点击"拒绝"，调用方应退出 App
 */
@Composable
fun PrivacyAgreementDialog(
    onAgree: () -> Unit,
    onDisagree: () -> Unit,
) {
    Dialog(
        onDismissRequest = { /* 不可关闭 */ },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                // 标题
                Text(
                    text = stringResource(R.string.privacy_agreement_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 正文（使用 LinkAnnotation 新 API，避免 ClickableText 废弃警告）
                val primaryColor = MaterialTheme.colorScheme.primary
                val welcome = stringResource(R.string.privacy_agreement_welcome)
                val contentPrefix = stringResource(R.string.privacy_agreement_content_prefix)
                val userAgreement = stringResource(R.string.privacy_agreement_user_agreement)
                val and = stringResource(R.string.privacy_agreement_and)
                val privacyPolicy = stringResource(R.string.privacy_agreement_privacy_policy)
                val contentSuffix = stringResource(R.string.privacy_agreement_content_suffix)
                val annotatedText = buildAnnotatedString {
                    append(welcome)
                    append("\n\n")
                    append(contentPrefix)
                    withLink(LinkAnnotation.Clickable(
                        tag = "USER_AGREEMENT",
                        styles = TextLinkStyles(style = SpanStyle(color = primaryColor, fontWeight = FontWeight.Medium)),
                        linkInteractionListener = {
                            // TODO: 跳转用户协议页面
                        },
                    )) {
                        append(userAgreement)
                    }
                    append(and)
                    withLink(LinkAnnotation.Clickable(
                        tag = "PRIVACY_POLICY",
                        styles = TextLinkStyles(style = SpanStyle(color = primaryColor, fontWeight = FontWeight.Medium)),
                        linkInteractionListener = {
                            // TODO: 跳转隐私政策页面
                        },
                    )) {
                        append(privacyPolicy)
                    }
                    append("。\n\n")
                    append(contentSuffix)
                }

                Text(
                    text = annotatedText,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 同意按钮
                Button(
                    onClick = onAgree,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(stringResource(R.string.privacy_agreement_agree))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 拒绝按钮
                OutlinedButton(
                    onClick = onDisagree,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(stringResource(R.string.privacy_agreement_disagree))
                }
            }
        }
    }
}
