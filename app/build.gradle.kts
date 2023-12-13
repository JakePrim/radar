plugins {
    alias(libs.plugins.appn.android.application)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.appn"
    defaultConfig {
        applicationId = "com.example.appn"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":core:core-network"))
    implementation(project(":feature:feature-base"))
}