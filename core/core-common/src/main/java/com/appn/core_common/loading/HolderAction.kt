package com.appn.core_common.loading

import android.view.View
import androidx.annotation.ColorRes

interface HolderAction {
    fun showLoading()
    fun showError()
    fun showSuccess()
    fun showEmpty()
    fun showStatus(status: Int)
    fun onBindToolbar(
        enable: Boolean = false,
        title: String = "",
        markerView: View? = null,
        isCover: Boolean = false
    ): Holder

    fun hideToolbar(): Holder

    fun showToolbar(): Holder
}

/**
 * 状态view点击回调
 */
typealias HRunnable = (Any) -> Unit

/**
 * 标题栏返回按钮回调
 */
typealias BackCallback = () -> Unit
/**
 * 标题栏功能按钮回调
 */
typealias FunctionCallback = (Any) -> Unit