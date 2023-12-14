package com.appn.core_common.loading

import android.view.View

abstract class LoadingAdapter : Adapter {
    override fun getToolBarView(holder: Holder, title: String, toolBarView: View?): View? {
        return null
    }

    override fun onBindToolBar(data: Any?) {
    }
}