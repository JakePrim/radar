# core-common

> common 层的设计思想，统一管理Activity/Fragment生命周期，处理基础业务，并提供给外部接口使用

![Flutter (1)](./assets/Flutter%20(1).png)



### ConfigModule

> Feature 模块的Application初始化接口类，如果Feature模块中有需要在Application中初始化的代码，就需要实现该接口，并注册到AppDelegate。

Feature 模块只需要实现ConfigeModule的接口，就可以监听Application、Activity、Fragment的生命周期，处理具体的业务

```kotlin
    /**
     * Application的声明周期注入
     */
    fun injectAppLifecycle(context: Context, lifecycles: List<AppLifecycles>)

    /**
     * Activity生命周期的注入操作
     */
    fun injectActivityLifecycle(
        context: Context,
        lifecycles: List<Application.ActivityLifecycleCallbacks>
    )


    /**
     * Fragment 的生命周期注入操作
     */
    fun injectFragmentLifecycle(
        context: Context,
        lifecycles: List<FragmentManager.FragmentLifecycleCallbacks>
    )
```

### BaseActivity

> BaseActivity 只实现了IActivity的接口方法，考量Java/Kotlin都是单继承，如果在项目开发中需要继承第三方的Activity，只需要实现IActivity的接口即可，不会影响框架的核心代码，而不是一慨而论将所有的基础代码都写到Base中去。

```kotlin
interface IActivity {

    /**
     * 初始化View
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化布局
     */
    fun initContentView(savedInstanceState: Bundle?)

    /**
     * Activity是否使用了Fragment，会更具这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     */
    fun useFragment(): Boolean

    fun useEvent(): Boolean

    /**
     * 字体大小是否跟随系统大小变化,默认为false不跟随系统字体大小变换
     */
    fun useFontScale(): Boolean
}
```



### BaseFragment

> BaseFragment和BaseActivity的设计考量一致，都是单继承需要考量到继承第三方的Activity而不是一慨而论将所有的基础代码都写到Base中去。

```kotlin
interface IFragment {

    /**
     * 是否使用EventBus,自动注册EventBus [FragmentDelegate] 中实现
     */
    fun useEvent(): Boolean

    /**
     * 初始化View
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * setData 通过此方法可以使Fragment与外界做一些交互和通信
     *  Example usage:
     *      * public void setData(@Nullable Object data) {
     *      *     if (data != null && data instanceof Message) {
     *      *         switch (((Message) data).what) {
     *      *             case 0:
     *      *                 loadData(((Message) data).arg1);
     *      *                 break;
     *      *             case 1:
     *      *                 refreshUI();
     *      *                 break;
     *      *             default:
     *      *                 //do something
     *      *                 break;
     *      *         }
     *      *     }
     *      * }
     *      *
     *      * // call setData(Object):
     *      * Message data = new Message();
     *      * data.what = 0;
     *      * data.arg1 = 1;
     *      * fragment.setData(data);
     */
    fun setData(data:Any)
}
```

