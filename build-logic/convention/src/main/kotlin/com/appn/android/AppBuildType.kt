package com.appn.android

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
                isMinifyEnabled = false
                isZipAlignEnabled = true
                isJniDebuggable = true
                isShrinkResources = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
            release {
                customFiled(false, releaseBaseUrl, releaseH5BaseUrl)
                isMinifyEnabled = true
                isJniDebuggable = false
                isZipAlignEnabled = true
                isShrinkResources = false
            }
            //添加新的buildType
            create("dev") {
                customFiled(true, devBaseUrl, devH5BaseUrl)
                isMinifyEnabled = false
                isZipAlignEnabled = true
                isJniDebuggable = true
                isShrinkResources = false
            }
        }
    }
}

/**
 * 自定义的属性 添加在这里
 */
private fun LibraryBuildType.customFiled(log: Boolean, baseUrl: String, baseH5Url: String) {
    buildConfigField("boolean", "LOG_DEBUG", "$log")
    buildConfigField("String", "BASE_URL", "$baseUrl")
    buildConfigField("String", "BASE_H5_URL", "$baseH5Url")
}