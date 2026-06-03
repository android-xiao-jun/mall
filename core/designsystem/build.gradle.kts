plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mall.core.designsystem"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core
    implementation(project(":core:common"))

    // Compose BOM
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.material3)
    api(libs.compose.material.icons.extended)
    api(libs.compose.foundation)
    api(libs.compose.runtime)

    debugApi(libs.compose.ui.tooling)

    // Coil
    api(libs.coil.compose)

}
