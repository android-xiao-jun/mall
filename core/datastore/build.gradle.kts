plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mall.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // DataStore
    api(libs.datastore.preferences)
    api(libs.datastore.core)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

}
