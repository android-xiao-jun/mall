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

    defaultConfig {
        applicationId = "com.example.mall"
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // MultiDex 配置：minSdk < 21 时必须启用
        // 即使 minSdk >= 21，显式声明可确保 AGP 生成优化的主 dex 划分
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            // 优先从 local.properties 读取签名配置（正式发布用）
            val localPropsFile = rootProject.file("local.properties")
            val localProps = Properties().apply {
                if (localPropsFile.exists()) load(localPropsFile.inputStream())
            }

            val storeFilePath = localProps.getProperty("STORE_FILE_PATH", "")
            val storePwd = localProps.getProperty("STORE_PASSWORD", "android")
            val alias = localProps.getProperty("KEY_ALIAS", "androiddebugkey")
            val keyPwd = localProps.getProperty("KEY_PASSWORD", "android")

            // 如果 local.properties 中未配置，则回退到项目自带的 debug-key.jks
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
