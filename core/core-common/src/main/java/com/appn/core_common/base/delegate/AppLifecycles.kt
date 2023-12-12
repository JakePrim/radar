package com.appn.core_common.base.delegate

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * 代理Application的生命周期
 */
interface AppLifecycles {
    fun attachBaseContext(context: Context)
    fun onCreate(application: Application)
    fun onTerminate(application: Application)
    fun onLowMemory(application: Application)
    fun configurationChanged(configuration: Configuration)
}