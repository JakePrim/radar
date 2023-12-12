# appN

> appN 是 Android 基于 MVI/MVVM 架构快速搭建Android Native App的基础模块化框架.

appN 框架目录划分：

- `app模块`：是应用程序模块，依赖所有模块，主要作用是初始化模块。
- `feature-模块`：功能特定的模块，其范围可以处理应用程序中的单一职责。这些模块可以在需要时被任何应用程序重用/测试，同时保持分离和隔离。
- `core-模块`：包含辅助代码和特定依赖项的公共库模块，需要在应用程序中的其他模块之间共享，可以依赖于其他的core模块，不能依赖于功能模块或应用程序模块。
- `LocalRepo`： 用于存放aar包
- `build-logic`: 公约gradle plugin插件
- `test`: 测试模块
- `lint`: 自定义代码提示模块，用于统一代码风格，后期可以实现
- `其他模块`: 例如和模块`sync`、以及 `app-nia-catalog`用于快速显示我们的设计系统的目录应用程序。

![a](./assets/a.png)

具体划分如下表所示：

| 模块名                                                       | 职责                                    | 备注                                                         |
| ------------------------------------------------------------ | --------------------------------------- | ------------------------------------------------------------ |
| **app**                                                      | 应用程序模块                            | 将所有feature(功能)模块整合在一起，并初始化feature模块       |
| **feature-base**                                             | 提供所有feature模块的公共服务接口依赖库 | feature模块公共的特性：bean和路由、数据请求等设置            |
| **feature-1**, **feature-2** ...                             | 功能特定的模块，遵循单一职责原则        |                                                              |
| **[core-base](http://172.28.24.128/appn/appbase/-/tree/master/core/core-base)** | core模块的基础公共库                    | core-base 主要用于提供接口，主要用来采集和测试core模块的操作 |
| **[core-common](http://172.28.24.128/appn/appbase/-/tree/master/core/core-common)** | 通用模块                                | 用来封装基础的Activity/Fragment等操作                        |
| **[core-network](http://172.28.24.128/appn/appbase/-/tree/master/core/core-network)** | 网络模块                                | 底层Retrofit+协程的网络请求封装：统一异常处理、返回结果统一处理 |
| **core-cache**                                               | 缓存模块                                | 处理本地缓存、Room数据库、SP/MMVK等缓存                      |
| **[core-ui](http://172.28.24.128/appn/appbase/-/tree/master/core/core-ui)** | UI模块                                  | UI库，包括常用的UI和自定义View、弹窗、屏幕适配等处理         |
| **core-theme**                                               | 主题切换、换肤等                        |                                                              |
| **core-share**                                               | 分享模块                                | 用来处理第三方分享：微信、QQ                                 |
| **core-web**                                                 | webview模块                             | WebView的封装处理,简化WebView的操作，支持多进程、复用池等功能 |
| **core-player**                                              | 多媒体模块                              | 用于视频、音频、直播的封装处理                               |
| **core-pay**                                                 | 支付模块                                | 支付基础库，封装支付的逻辑                                   |
| **core-log**                                                 | 日志打印模块                            | 用于日志打印、日志上传、日志浏览等功能                       |
| **core-permission**                                          | 权限管理模块                            | 用于权限的统一管理以及权限的弹窗提示等功能                   |
| **core-image**                                               | 图片模块                                | 用于图片的加载、大图展示缩放等功能实现                       |
| **core-encipher**                                            | 加密模块                                | AES Base64 加密                                              |
| **[core-router](http://172.28.24.128/appn/appbase/-/tree/master/core/core-router)** | 路由模块                                | 用来路由导航等                                               |
| **core-mqtt**                                                | MQTT                                    | 封装mqtt的功能通用代码                                       |
| **core-push**                                                | 推送                                    | 看能否统一推送功能，对外提供统一接口，实现不同的第三方推送功能 |
| **[core-utils](http://172.28.24.128/appn/appbase/-/tree/master/core/core-utils)** | 工具模块                                | 封装各种工具类：时间、校验、设备ID等等                       |
| **LocalRepo**                                                | 存放AAR                                 |                                                              |
| **build-logic**                                              | gradle plugin                           | 常用的gradle 公约插件:application/lib/module等 统一gralde配置 |
| **test**                                                     | 单元测试模块                            | 用于常见的测试和自测工作                                     |
| **lint**                                                     | 自定义的代码提示，用于统一代码风格      | 后期开发                                                     |

划分目录如下：

```
├─app
├─build-logic
│	└─convention
├─core
│	└─core-模块名
├─feature
│	└─feature-模块名
├─gradle
│  └─wrapper
├─lint
├─LocalRepo
│	└─AAR包
└─test
```

feature
模块根据使用情况进行依赖core模块，业务feature必须的依赖路径：`feature-模块` -> `feature-base`

第三方库管理方式：`gradle/libs.versions.toml` Google 官方推荐的管理方式，可以在模块下点击跳转，和Rust的第三包管理方式类似，非常好用。
如下代码 声明版本、libraries是引入的第三方库、plugins是引入的gradle插件

```toml
[versions]
agp = "8.2.0"
core-ktx = "1.10.1"

[libraries]
core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "core-ktx" }

[plugins]
androidApplication = { id = "com.android.application", version.ref = "agp" }
```

gradle统一的管理方式：推荐使用plugin进行管理，存放在`build-logic`下

- convention 定义公约插件：feature模块gradle公约、core模块gradle公约、应用程序模块gradle公约

对应的插件如下：

```toml
appn-android-core = { id = "appn.android.core", version = "unspecified" }
appn-android-feature = { id = "appn.android.feature", version = "unspecified" }
appn-android-application = { id = "appn.android.application", version = "unspecified" }
```

使用core模块的插件：

```groovy
plugins {
    //引入core的插件gradle公约
    alias(libs.plugins.appn.android.core)
}

android {
    namespace = "com.appn.core_ui"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
```

使用feature模块的插件：

```groovy
plugins {
    //引入feature模块的插件
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
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.android)
}
```

使用app模块的插件：

```groovy
plugins {
    alias(libs.plugins.appn.android.application)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.appn"
    defaultConfig {
        applicationId = "com.example.appn"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":core:core-network"))
}
```

- 定义buildType: 在convention/AppBuildType中定义进行统一管理

添加自定义属性

```kotlin
/**
 * 自定义的属性 添加在这里
 */
private fun BuildType.customFiled(log: Boolean, baseUrl: String, baseH5Url: String) {
    buildConfigField("boolean", "LOG_DEBUG", "$log")
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "BASE_H5_URL", "\"$baseH5Url\"")
}
```

添加除了debug和release的其他的buildType:

```kotlin
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
```

- SDK 版本管理：在convention/KotlinAndroid中 定义版本和JDK的版本配置

```kotlin
const val compileSdkVersion = 33

const val minSdkVersion = 21

const val targetVersion = 33
```

AndroidApplicationConventionPlugin ： 作为应用程序插件
AndroidCoreConventionPlugin：作为lib/core插件
AndroidFeatureConventionPlugin：作为module/feature插件

功能模块开发类文件的划分建议方式：

```
-Project
	-feature1
		-ui
		-domain
		-data
	-feature2
    	-ui
    	-domain
    	-data
   .... 	
```

