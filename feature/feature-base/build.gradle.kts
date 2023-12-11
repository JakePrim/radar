plugins {
    alias(libs.plugins.appn.android.feature)
}

android {
    namespace = "com.appn.feature-base"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    dataBinding { enable = true }
}

dependencies {
    implementation(":core:core-base")
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.android)
}