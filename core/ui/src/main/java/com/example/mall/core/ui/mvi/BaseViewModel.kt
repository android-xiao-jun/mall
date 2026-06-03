package com.example.mall.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVI BaseViewModel
 *
 * 设计原则：
 * - UiState: StateFlow 管理页面状态（持久化，新订阅者会收到最新值）
 * - UiEffect: Channel 管理一次性事件（如导航、Toast、Snackbar）
 * - Intent: SharedFlow 接收用户行为
 *
 * 使用方式：
 * ```
 * class HomeViewModel : BaseViewModel<HomeIntent, HomeUiState, HomeEffect>() {
 *     override fun createInitialState() = HomeUiState()
 *
 *     init {
 *         handleIntent { intent ->
 *             when (intent) {
 *                 is HomeIntent.LoadData -> loadData()
 *                 is HomeIntent.Refresh -> refresh()
 *             }
 *         }
 *     }
 * }
 * ```
 */
abstract class BaseViewModel<INTENT : UiIntent, STATE : UiState, EFFECT : UiEffect> : ViewModel() {

    // 页面状态 - StateFlow 保证新订阅者获取最新状态
    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    // 一次性事件 - Channel 保证事件只被消费一次
    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    // 用户行为 - SharedFlow
    private val _intent = MutableSharedFlow<INTENT>(extraBufferCapacity = 64)

    protected val currentState: STATE get() = _uiState.value

    /**
     * 创建初始状态
     */
    protected abstract fun createInitialState(): STATE

    /**
     * 处理 Intent
     * 子类在 init 块中调用
     */
    protected fun handleIntent(handler: suspend (INTENT) -> Unit) {
        viewModelScope.launch {
            _intent.collect { intent ->
                handler(intent)
            }
        }
    }

    /**
     * 发送 Intent
     * 由 UI 层调用
     */
    fun sendIntent(intent: INTENT) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    /**
     * 更新状态
     * 使用 reducer 模式，确保状态不可变
     */
    protected fun setState(reduce: STATE.() -> STATE) {
        _uiState.value = currentState.reduce()
    }

    /**
     * 发送一次性事件
     * 用于导航、Toast、Snackbar 等
     */
    protected fun sendEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * 在 IO 线程执行异步操作
     */
    protected fun launchIO(block: suspend () -> Unit) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            block()
        }
    }

    /**
     * 在 Main 线程执行操作
     */
    protected fun launchMain(block: suspend () -> Unit) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.Main) {
            block()
        }
    }
}

/**
 * UiState 基接口
 * 所有页面状态必须实现此接口
 */
interface UiState

/**
 * UiIntent 基接口
 * 所有用户行为必须实现此接口
 */
interface UiIntent

/**
 * UiEffect 基接口
 * 所有一次性行为必须实现此接口
 */
interface UiEffect
