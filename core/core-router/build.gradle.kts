plugins {
    alias(libs.plugins.appn.android.core)
}

android {
    namespace = "com.appn.core_router"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(":core:core-base")
}