package com.example.mall.core.player

import kotlinx.coroutines.flow.StateFlow

/**
 * 播放器状态
 */
sealed class PlayerState {
    data object Idle : PlayerState()
    data object Preparing : PlayerState()
    data class Playing(val position: Long = 0, val duration: Long = 0) : PlayerState()
    data class Paused(val position: Long = 0) : PlayerState()
    data object Completed : PlayerState()
    data class Error(val message: String) : PlayerState()
}

/**
 * 播放器类型
 */
enum class PlayerType {
    AUDIO,
    VIDEO,
    LIVE,
}

/**
 * 播放器接口
 *
 * 定义统一的播放器行为，音频/视频/直播播放器均实现此接口
 */
interface IPlayer {

    val state: StateFlow<PlayerState>

    val type: PlayerType

    fun prepare(url: String)

    fun play()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun setVolume(volume: Float)

    fun release()

    fun getCurrentPosition(): Long

    fun getDuration(): Long

    val isPlaying: Boolean
        get() = state.value is PlayerState.Playing
}
