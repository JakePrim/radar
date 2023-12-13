package com.appn.core_common.base.delegate

import android.os.Bundle

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