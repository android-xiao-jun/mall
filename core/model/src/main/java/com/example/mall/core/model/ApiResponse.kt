package com.example.mall.core.model

import kotlinx.serialization.Serializable

/**
 * 通用 API 响应基类
 *
 * 后端接口统一返回格式：
 * {
 *   "code": 0,
 *   "msg": "success",
 *   "data": T
 * }
 */
@Serializable
data class ApiResponse<T>(
    val code: Int = 0,
    val msg: String = "",
    val data: T? = null,
) {
    val isSuccessful: Boolean get() = code == CODE_SUCCESS

    companion object {
        const val CODE_SUCCESS = 0
        const val CODE_TOKEN_EXPIRED = 401
        const val CODE_FORBIDDEN = 403
        const val CODE_NOT_FOUND = 404
        const val CODE_SERVER_ERROR = 500
    }
}

/**
 * 分页请求参数
 */
@Serializable
data class PageRequest(
    val page: Int = 1,
    val pageSize: Int = 20,
) {
    val offset: Int get() = (page - 1) * pageSize
}

/**
 * 分页响应数据
 */
@Serializable
data class PageResponse<T>(
    val list: List<T> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 20,
    val hasMore: Boolean = false,
)

/**
 * 空响应，用于不需要返回数据的接口
 */
@Serializable
data object EmptyResponse
