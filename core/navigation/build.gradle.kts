plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.mall.core.navigation"
}

dependencies {
    implementation(project(":core:common"))

    // Navigation Compose
    api(libs.androidx.navigation.compose)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

}
