package com.appn.core_common.base.delegate

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager

/**
 * Feature模块Application初始化 接口类
 */
interface ConfigModule {

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
}