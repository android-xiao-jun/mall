plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mall.core.network"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))


    // Network
    api(libs.retrofit)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.kotlinx.serialization.json)
    api(libs.retrofit.kotlinx.serialization)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

}
