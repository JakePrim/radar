package com.appn.core_common.base.delegate

import android.app.Activity
import android.os.Bundle
import com.appn.core_common.integration.EventBusManager

class ActivityDelegateImpl(private val mActivity: Activity) : ActivityDelegate {
    private var mIActivity: IActivity = mActivity as IActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mIActivity.useEvent()) {
            EventBusManager.getInstance()?.register(mActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
    }

    override fun onStart() {
    }

    override fun onPause() {
    }

    override fun onResume() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
        if (mIActivity.useEvent()) {
            EventBusManager.getInstance()?.unregister(mActivity)
        }
    }
}