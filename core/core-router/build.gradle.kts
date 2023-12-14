@Suppress("DSL_SCOPE_VIOLATION")
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
    implementation(project(":core:core-base"))
}