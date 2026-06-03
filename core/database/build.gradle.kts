plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mall.core.database"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // Room
    api(libs.room.runtime)
    api(libs.room.ktx)
    api(libs.room.paging)
    ksp(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Kotlin Serialization (for TypeConverter)
    implementation(libs.kotlinx.serialization.json)

}
