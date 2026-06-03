plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mall.core.common"
}

dependencies {
    // Coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)

    // Timber
    api(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
}
