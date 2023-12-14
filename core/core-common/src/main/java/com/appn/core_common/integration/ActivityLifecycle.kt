package com.appn.core_common.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.appn.core_common.base.delegate.ActivityDelegate
import com.appn.core_common.base.delegate.ActivityDelegateImpl
import com.appn.core_common.base.delegate.AppDelegate
import com.appn.core_common.base.delegate.IActivity

/**
 * 监听Activity的生命周期实现类
 */
class ActivityLifecycle : Application.ActivityLifecycleCallbacks {
    private var cacheActivityDelegate = mutableMapOf<String, ActivityDelegate>()

    //框架内部的Fragment生命周期回调
    private val mFragmentLifecycle: Lazy<FragmentManager.FragmentLifecycleCallbacks> =
        lazyOf(FragmentLifecycle())

    //modules的Fragment生命周期回调
    private val mFragmentLifecycles =
        lazyOf(mutableListOf<FragmentManager.FragmentLifecycleCallbacks>())

    /**
     * 是否给Modules注入Fragment生命周期
     */
    private var isInjectFragmentLifecycle = false

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        //如果 intent 包含了此字段,并且为 true 说明不加入到 list 进行统一管理
        var isNotAdd = false
        if (activity.intent != null) {
            isNotAdd = activity.intent.getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false)
        }

        if (!isNotAdd) {
            AppManager.getAppManager().addActivity(activity)
        }
        //配置ActivityDelegate
        fetchActivityDelegate(activity)?.onCreate(bundle)
        //配置Fragment
        registerFragmentCallbacks(activity)
    }

    private fun fetchActivityDelegate(activity: Activity): ActivityDelegate? {
        var activityDelegate: ActivityDelegate? = null
        if (activity is IActivity) {
            //包名+类名 作为key
            activityDelegate = cacheActivityDelegate[activity.javaClass.name]
            if (activityDelegate == null) {
                activityDelegate = ActivityDelegateImpl(activity)
                cacheActivityDelegate[activity.javaClass.name] = activityDelegate
            }
        }
        return activityDelegate
    }

    private fun clearActivityDelegate(activity: Activity) {
        cacheActivityDelegate.remove(activity.javaClass.name)
    }

    override fun onActivityStarted(activity: Activity) {
        fetchActivityDelegate(activity)?.onStart()
    }

    override fun onActivityResumed(activity: Activity) {
        AppManager.getAppManager().currentActivity = activity
        fetchActivityDelegate(activity)?.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        fetchActivityDelegate(activity)?.onPause()
    }

    override fun onActivityStopped(activity: Activity) {
        fetchActivityDelegate(activity)?.onStop()
        if (AppManager.getAppManager().currentActivity == activity) {
            AppManager.getAppManager().currentActivity = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        fetchActivityDelegate(activity)?.onSaveInstanceState(bundle)
    }

    override fun onActivityDestroyed(activity: Activity) {
        AppManager.getAppManager().removeActivity(activity)
        fetchActivityDelegate(activity)?.onDestroy()
        clearActivityDelegate(activity)
    }

    /**
     * 给每个Activity的所有Fragment设置监听生命周期
     */
    private fun registerFragmentCallbacks(activity: Activity) {
        if (activity is IActivity) {
            val useFragment = activity.useFragment()
            if ((activity is FragmentActivity) && useFragment) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    mFragmentLifecycle.value,
                    true
                )
                if (!isInjectFragmentLifecycle && AppDelegate.mModules.isNotEmpty()) {
                    AppDelegate.mModules.forEach {
                        it.injectFragmentLifecycle(activity.application, mFragmentLifecycles.value)
                    }
                    //重置标记表示已经注入
                    isInjectFragmentLifecycle = true
                }
                //注册Moduler的FragmentLifecycle
                mFragmentLifecycles.value.forEach {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(it, true)
                }
            }
        }
    }
}