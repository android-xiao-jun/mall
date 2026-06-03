import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mall"

    // ============================================================
    // CI/CD 版本管理：支持从环境变量注入 versionCode / versionName
    // ============================================================
    // 本地开发：使用下面硬编码的默认值
    // CI 环境：通过 -PversionCode=xxx 或环境变量 CI_VERSION_CODE 覆盖
    val ciVersionCode: Int = (project.findProperty("versionCode") as? String)
        ?.toIntOrNull()
        ?: System.getenv("CI_VERSION_CODE")?.toIntOrNull()
        ?: 1

    val ciVersionName: String = (project.findProperty("versionName") as? String)
        ?: System.getenv("CI_VERSION_NAME")
        ?: "1.0.0"

    // Git 提交短哈希，用于构建溯源
    val gitSha: String = providers.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.get().trim().ifEmpty { "unknown" }

    // 构建时间戳
    val buildTime: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Date())

    // 是否在 CI 环境中运行
    val isCi = System.getenv("CI") == "true"

    defaultConfig {
        applicationId = "com.example.mall"
        targetSdk = 35
        versionCode = ciVersionCode
        versionName = ciVersionName

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // MultiDex 配置：minSdk < 21 时必须启用
        // 即使 minSdk >= 21，显式声明可确保 AGP 生成优化的主 dex 划分
        multiDexEnabled = true

        // ============================================================
        // CI/CD 构建溯源 BuildConfig 字段
        // ============================================================
        buildConfigField("String", "GIT_SHA", "\"$gitSha\"")
        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
        buildConfigField("Boolean", "CI_BUILD", "$isCi")
    }

    signingConfigs {
        create("release") {
            // ============================================================
            // 签名配置优先级：
            //   1. CI 环境变量（GitHub Actions 等）
            //   2. local.properties 本地配置
            //   3. 项目自带 debug-key.jks 兜底
            // ============================================================
            val ciStoreFilePath = System.getenv("KEYSTORE_PATH").orEmpty()
            val ciStorePwd = System.getenv("KEYSTORE_PASSWORD").orEmpty()
            val ciAlias = System.getenv("KEY_ALIAS").orEmpty()
            val ciKeyPwd = System.getenv("KEY_PASSWORD").orEmpty()

            val localPropsFile = rootProject.file("local.properties")
            val localProps = Properties().apply {
                if (localPropsFile.exists()) load(localPropsFile.inputStream())
            }

            val storeFilePath = ciStoreFilePath.ifEmpty { localProps.getProperty("STORE_FILE_PATH", "") }
            val storePwd = ciStorePwd.ifEmpty { localProps.getProperty("STORE_PASSWORD", "android") }
            val alias = ciAlias.ifEmpty { localProps.getProperty("KEY_ALIAS", "androiddebugkey") }
            val keyPwd = ciKeyPwd.ifEmpty { localProps.getProperty("KEY_PASSWORD", "android") }

            // 如果均未配置，则回退到项目自带的 debug-key.jks
            val resolvedStoreFile = if (storeFilePath.isNotEmpty()) file(storeFilePath) else file("../debug-key.jks")

            storeFile = resolvedStoreFile
            storePassword = storePwd
            this.keyAlias = alias
            this.keyPassword = keyPwd
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("String", "ENVIRONMENT", "\"DEV\"")
            buildConfigField("Boolean", "ENABLE_LOG", "true")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("String", "ENVIRONMENT", "\"PROD\"")
            buildConfigField("Boolean", "ENABLE_LOG", "false")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // ============================================================
    // CI/CD APK 输出文件名定制
    // 格式：mall-{versionName}-{versionCode}-{buildType}-{gitSha}.apk
    // 便于在 CI 产物中快速识别版本与来源
    // ============================================================
    applicationVariants.all {
        val variant = this
        val output = variant.outputs.first() as? com.android.build.gradle.internal.api.ApkVariantOutputImpl
        output?.outputFileName = "mall-${variant.versionName}-${variant.versionCode}-${variant.buildType.name}-${gitSha}.apk"
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:player"))

    // Feature modules
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:conversation"))
    implementation(project(":feature:voice_room"))
    implementation(project(":feature:live"))
    implementation(project(":feature:gift"))
    implementation(project(":feature:wallet"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:setting"))

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.tooling.preview)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // MultiDex（minSdk >= 21 时系统天然支持，但声明依赖以确保主 dex 划分优化）
    implementation(libs.androidx.multidex)

    // Baseline Profile 安装器（配合 CI Baseline Profile 生成任务）
    implementation(libs.profileinstaller)

    // Debug
    debugImplementation(libs.compose.ui.tooling)

//    // Testing
//    testImplementation(libs.junit5.api)
//    testRuntimeOnly(libs.junit5.engine)
//    testImplementation(libs.mockk)
//    testImplementation(libs.kotlinx.coroutines.test)
//    testImplementation(libs.turbine)
//
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.compose.bom))
//    androidTestImplementation(libs.compose.ui.test.junit4)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
