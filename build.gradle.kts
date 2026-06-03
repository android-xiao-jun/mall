// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

/**
 * 公共 Android 配置：统一管理 compileSdk / minSdk / Java 兼容性
 *
 * 所有 library 和 application 模块共享以下配置：
 * - compileSdk = 35
 * - minSdk = 24
 * - Java 11 / JVM Target 11
 *
 * 各模块只需声明 namespace，无需重复上述配置
 */
subprojects {
    afterEvaluate {
        if (plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")) {
            configure<com.android.build.gradle.BaseExtension> {
                compileSdkVersion(35)
                defaultConfig {
                    minSdk = 24
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }

        // 公共 Kotlin 编译选项
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }
}
