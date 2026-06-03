package com.example.mall.core.domain.usecase

import com.example.mall.core.common.dispatcher.IoDispatcher
import com.example.mall.core.common.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * BaseUseCase - 无参数，返回 Flow<Result<T>>
 */
abstract class BaseFlowUseCase<out T>(
    private val ioDispatcher: CoroutineDispatcher,
) {
    protected abstract fun execute(): Flow<Result<T>>

    operator fun invoke(): Flow<Result<T>> = execute()
        .flowOn(ioDispatcher)
}

/**
 * BaseUseCase - 带参数，返回 Flow<Result<T>>
 */
abstract class BaseParamFlowUseCase<in P, out T>(
    private val ioDispatcher: CoroutineDispatcher,
) {
    protected abstract fun execute(parameters: P): Flow<Result<T>>

    operator fun invoke(parameters: P): Flow<Result<T>> = execute(parameters)
        .flowOn(ioDispatcher)
}

/**
 * BaseUseCase - 无参数，返回 suspend Result<T>
 */
abstract class BaseSuspendUseCase<out T>(
    private val ioDispatcher: CoroutineDispatcher,
) {
    protected abstract suspend fun execute(): Result<T>

    suspend operator fun invoke(): Result<T> = withContext(ioDispatcher) {
        try {
            execute()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

/**
 * BaseUseCase - 带参数，返回 suspend Result<T>
 */
abstract class BaseParamSuspendUseCase<in P, out T>(
    private val ioDispatcher: CoroutineDispatcher,
) {
    protected abstract suspend fun execute(parameters: P): Result<T>

    suspend operator fun invoke(parameters: P): Result<T> = withContext(ioDispatcher) {
        try {
            execute(parameters)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
