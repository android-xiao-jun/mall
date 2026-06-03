package com.example.mall.core.common.result

/**
 * 统一结果封装，用于Repository层返回数据
 *
 * 设计原则：
 * - Success: 数据获取成功
 * - Error: 可预期的业务错误
 * - Loading: 加载中状态
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val code: Int? = null, val message: String? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * 将 Result 映射为新的数据类型
 */
fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
    is Result.Loading -> this
}

/**
 * 获取数据，如果为Error则返回null
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}

/**
 * 获取数据，如果为Error则返回默认值
 */
fun <T> Result<T>.getOrDefault(default: T): T = when (this) {
    is Result.Success -> data
    else -> default
}

/**
 * 获取数据，如果为Error则抛出异常
 */
fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error -> throw exception
    is Result.Loading -> throw IllegalStateException("Result is still loading")
}

/**
 * onSuccess 回调
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * onError 回调
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

/**
 * onLoading 回调
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}
