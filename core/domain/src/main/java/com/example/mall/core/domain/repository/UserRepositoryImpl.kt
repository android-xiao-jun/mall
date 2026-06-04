package com.example.mall.core.domain.repository

import com.example.mall.core.common.result.Result
import com.example.mall.core.datastore.UserDataStore
import com.example.mall.core.model.LoginDto
import com.example.mall.core.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UserRepository 的 Stub 实现
 * 后续替换为真实 API 调用
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
) : UserRepository {

    override suspend fun login(phone: String, code: String): Result<LoginDto> {
        // 模拟登录：直接返回成功，无需真实接口
        return Result.Success(
            LoginDto(
                token = "mock_token_${System.currentTimeMillis()}",
                refreshToken = "mock_refresh_token",
                expireTime = System.currentTimeMillis() + 3600_000,
                user = UserDto(
                    uid = "10001",
                    nickname = "测试用户",
                    avatar = "",
                    phone = phone,
                    level = 1,
                    vipLevel = 0,
                ),
            ),
        )
    }

    override suspend fun logout(): Result<Unit> {
        userDataStore.clearUserData()
        return Result.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): Result<LoginDto> {
        return Result.Success(
            LoginDto(
                token = "mock_new_token",
                refreshToken = "mock_new_refresh_token",
                expireTime = System.currentTimeMillis() + 3600_000,
                user = UserDto(
                    uid = "10001",
                    nickname = "测试用户",
                    avatar = "",
                    phone = "",
                    level = 1,
                    vipLevel = 0,
                ),
            ),
        )
    }

    override fun getUser(): Flow<Result<UserDto>> {
        return flow {
            emit(
                Result.Success(
                    UserDto(
                        uid = "10001",
                        nickname = "测试用户",
                        avatar = "",
                        phone = "",
                        level = 1,
                        vipLevel = 0,
                    ),
                ),
            )
        }
    }

    override suspend fun updateUser(user: UserDto): Result<UserDto> {
        return Result.Success(user)
    }

    override suspend fun isLoggedIn(): Boolean {
        return userDataStore.isLoginFlow().first()
    }
}

