@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.appn.android.feature)
}

android {
    namespace = "com.appn.feature_base"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
}