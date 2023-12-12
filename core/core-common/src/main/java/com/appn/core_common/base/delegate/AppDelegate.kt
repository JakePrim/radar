package com.appn.core_common.base.delegate

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Process
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


/**
 * [AppDelegate] 代理Application生命周期当遇到某些三方库需要继承于它的 Application 的时候,
 * 就只有自定义 Application 并继承于三方库的 Application 这时就不用再继承 BaseApplication,
 * 只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口),框架就能照常运行
 */
class AppDelegate(private val context: Context) : AppLifecycles {
    private val classInitList: ArrayList<Class<out ConfigModule>> = ArrayList()
    private val appInitList: ArrayList<ConfigModule> = ArrayList()
    private var mAppLifecycles: ArrayList<AppLifecycles> = ArrayList()
    private var mActivityLifecycles: ArrayList<ActivityLifecycleCallbacks> = ArrayList()
    private var mApplication: Application? = null
    protected var mActivityLifecycle: ActivityLifecycleCallbacks? =
        object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        }

    //更细粒度的判断App所处的状态
    private var mComponentCallback: ComponentCallbacks2? = null

    fun registerAppLifecycles(classInit: Class<out ConfigModule>) {
        classInitList.add(classInit)
    }

    init {
        classInitList.forEach { clazz ->
            val configModule = clazz.newInstance() as ConfigModule
            //外部实现者 将Application的生命周期回调注入到mAppLifecycles集合中
            configModule.injectAppLifecycle(context, mAppLifecycles)
            //外部实现者，将Activity生命周期回调注入到mActivityLifecycles集合中
            configModule.injectActivityLifecycle(context, mActivityLifecycles)
            appInitList.add(configModule)
        }
    }

    /**
     * 判断是否是当前主进程，在多进程App会初始化多次
     */
//    fun isMainProcess(): Boolean {
//        return BuildConfig.APPLICATION_ID.equals(getCurrentProcessNameByFile())
//    }
//
//    fun getCurrentProcessNameByFile(): String? {
//        return try {
//            val file = File("/proc/" + Process.myPid() + "/cmdline")
//            val mBufferedReader = BufferedReader(FileReader(file))
//            val processName = mBufferedReader.readLine().trim { it <= ' ' }
//            mBufferedReader.close()
//            processName
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//            "com.dayunauto.android" //TODO 有可能获取失败，需要写默认的进程名
//        }
//    }

    override fun attachBaseContext(context: Context) {
        mAppLifecycles.forEach {
            it.attachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        this.mApplication = application
        //注册框架内部已实现的 Activity 生命周期逻辑
        this.mApplication?.registerActivityLifecycleCallbacks(mActivityLifecycle)
        //扩展Activity的生命周期逻辑
        mActivityLifecycles.forEach {
            application.registerActivityLifecycleCallbacks(it)
        }
        mComponentCallback = AppComponentCallbacks(application)
        //外部调用者，扩展onCreate逻辑
        mAppLifecycles.forEach {
            it.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        if (mActivityLifecycle != null) {
            mApplication?.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
        }
        if (mActivityLifecycles.isNotEmpty()) {
            mActivityLifecycles.forEach {
                mApplication?.unregisterActivityLifecycleCallbacks(it)
            }
        }
        if (mAppLifecycles.isNotEmpty()) {
            mAppLifecycles.forEach {
                it.onTerminate(application)
            }
        }
    }

    override fun onLowMemory(application: Application) {
        mAppLifecycles.forEach {
            it.onLowMemory(application)
        }
    }

    override fun configurationChanged(configuration: Configuration) {
        mAppLifecycles.forEach {
            it.configurationChanged(configuration)
        }
    }

    /**
     * 在你的 App 生命周期的任何阶段, {@link ComponentCallbacks2#onTrimMemory(int)} 发生的回调都预示着你设备的内存资源已经开始紧张
     */
    inner class AppComponentCallbacks(application: Application) : ComponentCallbacks2 {
        override fun onConfigurationChanged(p0: Configuration) {

        }

        /**
         * 当系统开始清除 LRU 列表中的进程时, 尽管它会首先按照 LRU 的顺序来清除, 但是它同样会考虑进程的内存使用量, 因此消耗越少的进程则越容易被留下来
         * {@link ComponentCallbacks2#onTrimMemory(int)} 的回调是在 API 14 才被加进来的, 对于老的版本, 你可以使用 {@link ComponentCallbacks2#onLowMemory} 方法来进行兼容
         * {@link ComponentCallbacks2#onLowMemory} 相当于 {@code onTrimMemory(TRIM_MEMORY_COMPLETE)}
         */
        override fun onLowMemory() {
            //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
        }

        override fun onTrimMemory(level: Int) {
            //状态1. 当开发者的 App 正在运行
            //设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发杀死 LRU 列表中的进程的机制
//                case TRIM_MEMORY_RUNNING_MODERATE:


            //设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用来提升系统性能 (但是这也会直接影响到你的 App 的性能)
//                case TRIM_MEMORY_RUNNING_LOW:


            //设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该立即释放所有非必须的资源
            //如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
//                case TRIM_MEMORY_RUNNING_CRITICAL:


            //状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
//                case TRIM_MEMORY_UI_HIDDEN:


            //状态3. 当前的 App 进程被置于 Background LRU 列表中
            //进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉 LRU 列表中的其他进程了
            //你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速恢复
//                case TRIM_MEMORY_BACKGROUND:


            //系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张, 你的进程是有可能被杀死的
//                case TRIM_MEMORY_MODERATE:


            //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
            //低于 API 14 的 App 可以使用 onLowMemory 回调
//                case TRIM_MEMORY_COMPLETE:
        }

    }
}