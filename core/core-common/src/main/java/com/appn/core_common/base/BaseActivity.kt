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

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}