plugins {
    alias(libs.plugins.appn.android.core)
    id("kotlinx-serialization")
}

android {
    namespace = "com.appn.core_network"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(":core:core-base")
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    api(libs.kotlinx.serialization.json)
    api(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.gson)
}