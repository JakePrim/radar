## 公约插件
该文件夹定义了特定于项目的约定插件，用于保持单个 常见模块配置的来源。build-logic
通过在 中设置约定插件，我们可以避免重复的构建脚本设置， 杂乱的配置，没有目录的陷阱。build-logic subproject buildSrc

build-logic是包含在 settings.gradle.kts 中配置的 build。

里面是一个模块，它定义了一组所有正常的插件 模块可用于配置自身。build-logicconvention

build-logic还包括一组用于在插件本身之间共享逻辑的文件， 这对于使用共享配置 Android 组件（库与应用程序）。

这些插件是可添加和可组合的，并且尝试只完成一项职责。 然后，模块可以选择所需的配置。 如果模块存在没有共享代码的一次性逻辑，则最好直接定义该逻辑 在模块的 中，而不是使用特定于模块的 设置。build.gradle

当前约定插件列表：
appn.android.application、
appn.android.library、
appn.android.test： 配置常见的 Android 和 Kotlin 选项。

