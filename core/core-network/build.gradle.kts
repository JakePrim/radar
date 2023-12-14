@Suppress("DSL_SCOPE_VIOLATION")
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
    implementation(project(":core:core-base"))
    implementation(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.serialization.json)
    api(libs.retrofit)
    api(libs.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.kotlin.serialization)
    //测试相关
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
}