package com.example.mall.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {

    private const val DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"
    private const val DATE_FORMAT_DATE = "yyyy-MM-dd"
    private const val DATE_FORMAT_TIME = "HH:mm:ss"
    private const val DATE_FORMAT_MESSAGE = "yyyy-MM-dd HH:mm"

    fun formatTimestamp(
        timestamp: Long,
        pattern: String = DATE_FORMAT_DEFAULT,
    ): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)
    }

    fun formatMessageTime(timestamp: Long): String {
        return formatTimestamp(timestamp, DATE_FORMAT_MESSAGE)
    }

    fun formatDate(timestamp: Long): String {
        return formatTimestamp(timestamp, DATE_FORMAT_DATE)
    }

    fun formatTime(timestamp: Long): String {
        return formatTimestamp(timestamp, DATE_FORMAT_TIME)
    }

    /**
     * 格式化时长 (e.g. 1:23:45)
     */
    fun formatDuration(durationMs: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60

        return when {
            hours > 0 -> String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
            else -> String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    /**
     * 判断是否是今天
     */
    fun isToday(timestamp: Long): Boolean {
        val today = formatDate(System.currentTimeMillis())
        val target = formatDate(timestamp)
        return today == target
    }

    /**
     * 获取相对时间描述 (e.g. "刚刚", "5分钟前", "1小时前")
     */
    fun getRelativeTime(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "刚刚"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}分钟前"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}小时前"
            diff < TimeUnit.DAYS.toMillis(30) -> "${TimeUnit.MILLISECONDS.toDays(diff)}天前"
            else -> formatDate(timestamp)
        }
    }
}

object AmountUtils {

    /**
     * 格式化金额，保留2位小数
     */
    fun formatAmount(amount: Double): String {
        return String.format(Locale.getDefault(), "%.2f", amount)
    }

    /**
     * 格式化金额，添加货币符号
     */
    fun formatCurrency(amount: Double, symbol: String = "¥"): String {
        return "$symbol${formatAmount(amount)}"
    }

    /**
     * 格式化钻石/金币等虚拟币，使用逗号分隔
     */
    fun formatCoins(coins: Long): String {
        return String.format(Locale.getDefault(), "%,d", coins)
    }
}

object StringUtils {

    /**
     * 手机号脱敏
     * 13812345678 -> 138****5678
     */
    fun maskPhone(phone: String): String {
        if (phone.length != 11) return phone
        return "${phone.substring(0, 3)}****${phone.substring(7)}"
    }

    /**
     * 邮箱脱敏
     * test@example.com -> t***@example.com
     */
    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email
        val name = parts[0]
        val domain = parts[1]
        return if (name.length > 1) {
            "${name.first()}***@$domain"
        } else {
            email
        }
    }

    /**
     * 身份证脱敏
     */
    fun maskIdCard(idCard: String): String {
        if (idCard.length < 8) return idCard
        return "${idCard.substring(0, 4)}****${idCard.substring(idCard.length - 4)}"
    }
}
