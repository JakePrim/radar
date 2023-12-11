plugins {
    alias(libs.plugins.appn.android.core)
}

android {
    namespace = "com.appn.core_base"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}