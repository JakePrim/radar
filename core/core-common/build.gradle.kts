plugins {
    alias(libs.plugins.appn.android.core)
}

android {
    namespace = "com.appn.core_common"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:core-base"))
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.eventbus)
}