package com.example.mall.core.common.extension

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import com.example.mall.core.common.result.Result

// ==================== Context Extensions ====================

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

// ==================== Flow Extensions ====================

/**
 * 将 Flow<T> 转换为 Flow<Result<T>>
 * 自动处理 Loading 和 Error 状态
 */
fun <T> Flow<T>.asResult(
    coroutineDispatcher: CoroutineDispatcher? = null,
): Flow<Result<T>> {
    val source = if (coroutineDispatcher != null) {
        this@asResult
    } else {
        this@asResult
    }
    return source
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

// ==================== Coroutine Extensions ====================

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T {
    return withContext(kotlinx.coroutines.Dispatchers.IO, block)
}

suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T {
    return withContext(kotlinx.coroutines.Dispatchers.Default, block)
}

suspend fun <T> withMain(block: suspend CoroutineScope.() -> T): T {
    return withContext(kotlinx.coroutines.Dispatchers.Main, block)
}

// ==================== String Extensions ====================

fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

// ==================== Collection Extensions ====================

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

// ==================== Any Extensions ====================

fun Any?.isNull(): Boolean = this == null

fun Any?.isNotNull(): Boolean = this != null
