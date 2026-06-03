plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.mall.core.model"
}

dependencies {
    // Kotlin Serialization
    api(libs.kotlinx.serialization.json)

    // Paging
    api(libs.paging.runtime)

}
