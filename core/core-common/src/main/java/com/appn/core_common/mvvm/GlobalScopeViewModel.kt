package com.appn.core_common.mvvm

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 带有全局作用域的自定义viewModel
 */
class GlobalScopeViewModel : ViewModelStoreOwner {
    companion object {
        private var globalScopeViewModel: GlobalScopeViewModel? = null

        fun getInstance(): GlobalScopeViewModel {
            synchronized(this) {
                if (globalScopeViewModel == null) {
                    globalScopeViewModel = GlobalScopeViewModel()
                }
            }
            return globalScopeViewModel!!
        }
    }

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}