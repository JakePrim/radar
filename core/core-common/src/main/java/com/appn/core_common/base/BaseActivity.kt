package com.appn.core_common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appn.core_common.base.delegate.IActivity

/**
 * Activity的基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {
    companion object {
        val TAG = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView(savedInstanceState)
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun useFragment(): Boolean {
        return false
    }

    /**
     * 是否使用EventBus，默认返回true，自动检测当前Activity是否用到了EventBus，并且自动注册
     */
    override fun useEvent(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}