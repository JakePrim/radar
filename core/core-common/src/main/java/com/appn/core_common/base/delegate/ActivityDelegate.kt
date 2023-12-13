package com.appn.core_common.base.delegate

import android.os.Bundle


/**
 * Activity的生命周期代理类，用于框架内部在每个Activity的对应生命周期中插入需要的逻辑
 */
interface ActivityDelegate {
    fun onCreate(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle?)
    fun onStart()
    fun onPause()
    fun onResume()
    fun onStop()
    fun onDestroy()
}