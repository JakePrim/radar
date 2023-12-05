# appN
> appN 是 Android 基于 MVI 架构快速搭建Android Native App的基础模块化框架. 
> appN的命名由来：N 代表从0到n,N代表无穷大，也代表app在基建上可以无线扩展。

appN的功能划分：

- `app模块`：是应用程序模块，依赖所有模块，主要作用是初始化模块。
- `feature-模块`：功能特定的模块，其范围可以处理应用程序中的单一职责。这些模块可以在需要时被任何应用程序重用/测试，同时保持分离和隔离。
- `core-模块`：包含辅助代码和特定依赖项的公共库模块，需要在应用程序中的其他模块之间共享，可以依赖于其他的core模块，不能依赖于功能模块或应用程序模块。
- `其他模块`： 例如和模块`sync`、`benchmark`、 `test`以及 `app-nia-catalog`用于快速显示我们的设计系统的目录应用程序。

appN 更多的是帮助开发者提供通用的模块：通用的功能模块和核心模块，帮助开发者更高效率的同时保证高质量的代码开发App。

| 模块名                       | 职责                                    | 备注                                                         |
| ---------------------------- | --------------------------------------- | ------------------------------------------------------------ |
| app                          | 应用程序模块                            | 将所有feature(功能)模块整合在一起，并初始化feature模块       |
| `feature-service`            | 提供所有feature模块的公共服务接口依赖库 | feature模块公共的特性：bean和路由、数据请求等设置            |
| `feature-1,` `feature-2` ... | 功能特定的模块，遵循单一职责原则        |                                                              |
| `core-base`                  | core模块的基础公共库                    | core-base 主要用于提供接口，主要用来采集和测试core模块的操作 |
| `core-common`                | 通用模块                                | 用来封装基础的Activity/Fragment等操作                        |
| `core-network`               | 网络模块                                | 底层Retrofit+协程的网络请求封装：统一异常处理、返回结果统一处理 |
| `core-cache`                 | 缓存模块                                | 处理本地缓存、Room数据库、SP/MMVK等缓存                      |
| `core-ui`                    | UI模块                                  | UI库，包括常用的UI和自定义View、弹窗、屏幕适配等处理         |
| `core-theme`                 | 主题切换、换肤等                        |                                                              |
| `core-share`                 | 分享模块                                | 用来处理第三方分享：微信、QQ                                 |
| `core-web`                   | webview模块                             | WebView的封装处理,简化WebView的操作，支持多进程、复用池等功能 |
| `core-player`                | 多媒体模块                              | 用于视频、音频、直播的封装处理                               |
| `core-pay`                   | 支付模块                                | 支付基础库，封装支付的逻辑                                   |
| `core-log`                   | 日志打印模块                            | 用于日志打印、日志上传、日志浏览等功能                       |
| `core-permission`            | 权限管理模块                            | 用于权限的统一管理以及权限的弹窗提示等功能                   |
| `core-image`                 | 图片模块                                | 用于图片的加载、大图展示缩放等功能实现                       |
| `core-encipher`              | 加密模块                                | AES Base64 加密                                              |
| `core-router`                | 路由模块                                | 用来路由导航等                                               |

feature 模块根据使用情况进行依赖core模块，业务feature必须的依赖路径：`feature-模块` -> `feature-service` 

类文件的划分建议方式：

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

