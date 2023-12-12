package com.appn.android

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

//API base url
const val debugBaseUrl = ""
const val devBaseUrl = ""
const val releaseBaseUrl = ""

//H5 base url
const val devH5BaseUrl = ""
const val debugH5BaseUrl = ""
const val releaseH5BaseUrl = ""

internal fun Project.configureBuildType(extension: LibraryExtension) {
    extension.apply {
        buildTypes {
            debug {
                customFiled(true, debugBaseUrl, debugH5BaseUrl)
                debugSetting(this, this@apply)
            }
            release {
                customFiled(false, releaseBaseUrl, releaseH5BaseUrl)
                releaseSetting(this, this@apply)
            }
            //添加新的buildType
            create("dev") {
                customFiled(true, devBaseUrl, devH5BaseUrl)
                debugSetting(this, this@apply)
            }
        }
    }
}

internal fun Project.configureAppBuildType(extension: ApplicationExtension) {
    extension.apply {
        buildTypes {
            debug {
                customFiled(true, debugBaseUrl, debugH5BaseUrl)
                debugSetting(this, this@apply)
            }
            release {
                customFiled(false, releaseBaseUrl, releaseH5BaseUrl)
                releaseSetting(this, this@apply)
            }
            //添加新的buildType
            create("dev") {
                customFiled(true, devBaseUrl, devH5BaseUrl)
                debugSetting(this, this@apply)
            }
        }
    }
}

private fun debugSetting(
    buildType: BuildType,
    extension: CommonExtension<*, *, *, *, *>
) {
    buildType.isMinifyEnabled = false
    buildType.isZipAlignEnabled = true
    buildType.isJniDebuggable = true
    buildType.isShrinkResources = false
    buildType.proguardFiles(
        extension.getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
    )
}

private fun releaseSetting(
    buildType: BuildType,
    extension: CommonExtension<*, *, *, *, *>
) {
    buildType.isMinifyEnabled = true
    buildType.isJniDebuggable = false
    buildType.isZipAlignEnabled = true
    buildType.isShrinkResources = false
    buildType.proguardFiles(
        extension.getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
    )
}

/**
 * 自定义的属性 添加在这里
 */
private fun BuildType.customFiled(log: Boolean, baseUrl: String, baseH5Url: String) {
    buildConfigField("boolean", "LOG_DEBUG", "$log")
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "BASE_H5_URL", "\"$baseH5Url\"")
}