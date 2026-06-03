package com.example.mall.core.network.exception

/**
 * 网络异常统一封装
 */
sealed class NetworkException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {

    /** 网络连接异常 */
    data class ConnectivityException(
        override val message: String = "网络连接不可用，请检查网络设置",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 请求超时 */
    data class TimeoutException(
        override val message: String = "请求超时，请稍后重试",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 服务器错误 */
    data class ServerException(
        val code: Int,
        override val message: String = "服务器开小差了，请稍后重试",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 业务异常（后端返回的错误码） */
    data class BusinessException(
        val code: Int,
        override val message: String,
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** Token 过期 */
    data class TokenExpiredException(
        override val message: String = "登录已过期，请重新登录",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 未知异常 */
    data class UnknownException(
        override val message: String = "未知错误",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 请求被取消 */
    data class CancelledException(
        override val message: String = "请求已取消",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /** 序列化/反序列化异常 */
    data class SerializationException(
        override val message: String = "数据解析异常",
        override val cause: Throwable? = null,
    ) : NetworkException(message, cause)
}

/**
 * 将 Throwable 转换为用户友好的错误信息
 */
fun Throwable.toUserMessage(): String = when (this) {
    is NetworkException.ConnectivityException -> message
    is NetworkException.TimeoutException -> message
    is NetworkException.ServerException -> message
    is NetworkException.BusinessException -> message
    is NetworkException.TokenExpiredException -> message
    is NetworkException.CancelledException -> message
    is java.net.UnknownHostException -> "网络连接不可用，请检查网络设置"
    is java.net.SocketTimeoutException -> "请求超时，请稍后重试"
    is java.net.ConnectException -> "网络连接异常，请稍后重试"
    is kotlinx.serialization.SerializationException -> "数据解析异常"
    is retrofit2.HttpException -> {
        when (code()) {
            401 -> "登录已过期，请重新登录"
            403 -> "没有访问权限"
            404 -> "请求的资源不存在"
            in 500..599 -> "服务器开小差了，请稍后重试"
            else -> "网络请求异常(${code()})"
        }
    }
    is java.util.concurrent.CancellationException -> "请求已取消"
    else -> "未知错误：${message ?: ""}"
}
