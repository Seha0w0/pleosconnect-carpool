plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.pleosconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pleosconnect"
        minSdk = 26
        targetSdk = 35
        versionCode = 4
        versionName = "1.2.4"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("ai.pleos.playground:Vehicle:2.0.3")
}
