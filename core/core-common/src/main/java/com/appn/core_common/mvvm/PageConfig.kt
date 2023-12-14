package com.appn.core_common.mvvm

import android.util.SparseArray
import com.appn.core_common.loading.Adapter
import com.appn.core_common.loading.LoadingHelper

/**
 * 页面配置：Activity/Fragment 将页面配置独立出来方便管理
 * @param layout 页面布局id
 * @param vmVariableId layout的viewModel id
 */
class PageConfig(val layout: Int) {
    /**
     * 设置页面参数DataBinding的参数
     */
    private val bindingParams = SparseArray<Any>()

    private var loadingHelper = LoadingHelper.getDefault()

    /**
     * 页面初始数据配置
     */
    val pageData = mutableMapOf<String, Any>()

    /**
     * 获取binding的参数
     */
    fun getBindingParams(): SparseArray<Any> {
        return bindingParams
    }

    fun getLoadingHelper(): LoadingHelper {
        return loadingHelper!!
    }

    /**
     * 设置页面状态,默认的为Default,选择其他的适配器可以进行设置
     */
    fun setLoadingStatusType(viewType: String): PageConfig {
        loadingHelper = LoadingHelper.get(viewType)
        return this
    }

    /**
     * 设置Loading的Adapter
     */
    fun changeLoadingAdapter(viewType: String, adapter: Adapter): PageConfig {
        LoadingHelper.getDefault()?.register(viewType, adapter)
        loadingHelper = LoadingHelper.get(viewType)
        return this
    }


    /**
     * 添加binding参数
     */
    fun addBindingParams(variableId: Int, value: Any): PageConfig {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, value)
        }
        return this
    }

    /**
     * 添加页面配置数据
     */
    fun addPageData(k: String, v: Any): PageConfig {
        pageData[k] = v
        return this
    }
}