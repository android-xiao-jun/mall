package com.example.mall.core.network

/**
 * 多环境配置
 *
 * 环境切换通过 BuildConfig 或 DataStore 配置
 */
enum class Environment(val baseUrl: String, val wsUrl: String, val envName: String) {
    DEV(
        baseUrl = "https://dev-api.example.com/",
        wsUrl = "wss://dev-ws.example.com/",
        envName = "开发环境",
    ),
    TEST(
        baseUrl = "https://test-api.example.com/",
        wsUrl = "wss://test-ws.example.com/",
        envName = "测试环境",
    ),
    PRE(
        baseUrl = "https://pre-api.example.com/",
        wsUrl = "wss://pre-ws.example.com/",
        envName = "预发环境",
    ),
    PROD(
        baseUrl = "https://api.example.com/",
        wsUrl = "wss://ws.example.com/",
        envName = "生产环境",
    ),
}
