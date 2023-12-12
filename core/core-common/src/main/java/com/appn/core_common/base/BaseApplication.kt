package com.appn.core_common.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.appn.core_common.base.delegate.AppDelegate

class BaseApplication : Application() {
    private var mAppDelegate: AppDelegate? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (this.mAppDelegate == null) {
            this.mAppDelegate = AppDelegate(base)
        }
        this.mAppDelegate?.attachBaseContext(baseContext)
    }

    override fun onCreate() {
        super.onCreate()
        this.mAppDelegate?.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        this.mAppDelegate?.onTerminate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.mAppDelegate?.onLowMemory(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        this.mAppDelegate?.configurationChanged(newConfig)
    }
}