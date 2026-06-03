package com.example.mall.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mall.core.common.auth.TokenProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences",
)

/**
 * 用户信息 DataStore
 *
 * 存储：Token、用户基本信息、App配置、多语言配置
 */
@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) : TokenProvider {
    private val dataStore = context.userDataStore

    companion object {
        // Token
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val KEY_TOKEN_EXPIRE_TIME = longPreferencesKey("token_expire_time")

        // User Info
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_NICKNAME = stringPreferencesKey("user_nickname")
        val KEY_USER_AVATAR = stringPreferencesKey("user_avatar")
        val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        val KEY_USER_LEVEL = longPreferencesKey("user_level")
        val KEY_USER_VIP_LEVEL = longPreferencesKey("user_vip_level")

        // App Config
        val KEY_ENVIRONMENT = stringPreferencesKey("environment")
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_AGREEMENT_ACCEPTED = stringPreferencesKey("agreement_accepted")
        val KEY_FIRST_LAUNCH = stringPreferencesKey("first_launch")
    }

    // ==================== Token ====================

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[KEY_ACCESS_TOKEN] = token }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()
    }

    fun getTokenFlow(): Flow<String?> {
        return dataStore.data.map { it[KEY_ACCESS_TOKEN] }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { it[KEY_REFRESH_TOKEN] = refreshToken }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()
    }

    suspend fun saveTokenExpireTime(expireTime: Long) {
        dataStore.edit { it[KEY_TOKEN_EXPIRE_TIME] = expireTime }
    }

    fun getTokenExpireTimeFlow(): Flow<Long> {
        return dataStore.data.map { it[KEY_TOKEN_EXPIRE_TIME] ?: 0L }
    }

    suspend fun isTokenExpired(): Boolean {
        val expireTime = dataStore.data.map { it[KEY_TOKEN_EXPIRE_TIME] ?: 0L }.first()
        return System.currentTimeMillis() >= expireTime
    }

    // ==================== User Info ====================

    suspend fun saveUserId(userId: String) {
        dataStore.edit { it[KEY_USER_ID] = userId }
    }

    fun getUserIdFlow(): Flow<String?> {
        return dataStore.data.map { it[KEY_USER_ID] }
    }

    suspend fun saveUserNickname(nickname: String) {
        dataStore.edit { it[KEY_USER_NICKNAME] = nickname }
    }

    fun getUserNicknameFlow(): Flow<String?> {
        return dataStore.data.map { it[KEY_USER_NICKNAME] }
    }

    suspend fun saveUserAvatar(avatar: String) {
        dataStore.edit { it[KEY_USER_AVATAR] = avatar }
    }

    fun getUserAvatarFlow(): Flow<String?> {
        return dataStore.data.map { it[KEY_USER_AVATAR] }
    }

    suspend fun saveUserPhone(phone: String) {
        dataStore.edit { it[KEY_USER_PHONE] = phone }
    }

    // ==================== App Config ====================

    suspend fun saveEnvironment(env: String) {
        dataStore.edit { it[KEY_ENVIRONMENT] = env }
    }

    fun getEnvironmentFlow(): Flow<String> {
        return dataStore.data.map { it[KEY_ENVIRONMENT] ?: "PROD" }
    }

    suspend fun saveLanguage(language: String) {
        dataStore.edit { it[KEY_LANGUAGE] = language }
    }

    fun getLanguageFlow(): Flow<String> {
        return dataStore.data.map { it[KEY_LANGUAGE] ?: "zh" }
    }

    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { it[KEY_THEME_MODE] = mode }
    }

    fun getThemeModeFlow(): Flow<String> {
        return dataStore.data.map { it[KEY_THEME_MODE] ?: "system" }
    }

    suspend fun setAgreementAccepted(accepted: Boolean) {
        dataStore.edit { it[KEY_AGREEMENT_ACCEPTED] = accepted.toString() }
    }

    fun isAgreementAcceptedFlow(): Flow<Boolean> {
        return dataStore.data.map { it[KEY_AGREEMENT_ACCEPTED]?.toBoolean() ?: false }
    }

    suspend fun setFirstLaunch(firstLaunch: Boolean) {
        dataStore.edit { it[KEY_FIRST_LAUNCH] = firstLaunch.toString() }
    }

    fun isFirstLaunchFlow(): Flow<Boolean> {
        return dataStore.data.map { it[KEY_FIRST_LAUNCH]?.toBoolean() ?: true }
    }

    // ==================== Clear ====================

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_TOKEN_EXPIRE_TIME)
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USER_NICKNAME)
            preferences.remove(KEY_USER_AVATAR)
            preferences.remove(KEY_USER_PHONE)
            preferences.remove(KEY_USER_LEVEL)
            preferences.remove(KEY_USER_VIP_LEVEL)
        }
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
