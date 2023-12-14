package com.appn.core_common.mvvm

import android.os.Bundle
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.appn.core_common.base.BaseActivity
import com.appn.core_common.loading.Holder
import com.appn.core_common.loading.LoadingHelper

/**
 * 以DataBinding和ViewModel实现的MVVM模式
 */
abstract class BaseMvvmActivity<VB : ViewDataBinding> : BaseActivity() {
    protected lateinit var mBinding: VB
    protected lateinit var pageConfig: PageConfig
    protected lateinit var loadingHelper: LoadingHelper
    private var pageData = mutableMapOf<String, Any>()
    protected var loadingHolder: Holder? = null

    protected val mActivityProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }

    protected val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(GlobalScopeViewModel.getInstance())
    }

    /**
     * 页面配置信息
     */
    protected abstract fun pageConfig(): PageConfig

    /**
     * 给Holder赋值，控制页面的显示状态
     */
    open fun loadingHolder() {
        loadingHolder = loadingHelper.wrap(this).withRetry {
            retry()
        }
    }

    /**
     * 是否启用页面加载状态
     */
    open fun useLoading(): Boolean {
        return false
    }

    /**
     * 错误重试回调方法
     */
    open fun retry() {

    }

    /**
     * 页面处于加载中的状态
     */
    open fun pageLoading() {
        loadingHolder?.showLoading()
    }

    /**
     * 页面处于错误的状态
     */
    open fun pageError(data: Any) {
        loadingHolder?.withData(data)?.showError()
    }

    /**
     * 页面处于空数据的状态
     */
    open fun pageEmpty(data: Any) {
        loadingHolder?.withData(data)?.showEmpty()
    }

    /**
     * 页面处于业务正确的状态
     */
    open fun pageSuccess() {
        loadingHolder?.showSuccess()
    }

    override fun initContentView(savedInstanceState: Bundle?) {
        pageConfig = pageConfig()
        pageData = pageConfig.pageData
        mBinding = DataBindingUtil.setContentView(this, pageConfig.layout)
        mBinding.lifecycleOwner = this
        val bindingParams: SparseArray<Any> = pageConfig.getBindingParams()
        bindingParams.forEach { key, value ->
            mBinding.setVariable(key, value)
        }
        loadingHelper = pageConfig.getLoadingHelper()
        //配置loading
        if (useLoading()) {
            loadingHolder()
        }
    }

    /**
     * 获取在activity作用域的viewModel
     */
    protected open fun getActivityScopeViewModel(modelClass: Class<BaseViewModel>): BaseViewModel {
        return mActivityProvider[modelClass]
    }

    /**
     * 获取在全局作用域的viewModel
     */
    protected open fun getApplicationScopeViewModel(modelClass: Class<BaseViewModel>): BaseViewModel {
        return mApplicationProvider[modelClass]
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }
}