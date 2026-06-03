package com.example.mall.core.domain.repository

import com.example.mall.core.common.result.Result
import com.example.mall.core.model.LoginDto
import com.example.mall.core.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UserRepository 的 Stub 实现
 * 后续替换为真实 API 调用
 */
@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {

    override suspend fun login(phone: String, code: String): Result<LoginDto> {
        // TODO: 接入真实 API
        return Result.Success(
            LoginDto(
                token = "stub_token",
                refreshToken = "stub_refresh_token",
                expireTime = System.currentTimeMillis() + 3600_000,
                user = UserDto(
                    uid = "stub_uid",
                    nickname = "用户$phone",
                    avatar = "",
                    phone = phone,
                    level = 1,
                    vipLevel = 0,
                ),
            ),
        )
    }

    override suspend fun logout(): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): Result<LoginDto> {
        return Result.Success(
            LoginDto(
                token = "stub_new_token",
                refreshToken = "stub_new_refresh_token",
                expireTime = System.currentTimeMillis() + 3600_000,
                user = UserDto(
                    uid = "stub_uid",
                    nickname = "Stub User",
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
                        uid = "stub_uid",
                        nickname = "Stub User",
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
        return false
    }
}
