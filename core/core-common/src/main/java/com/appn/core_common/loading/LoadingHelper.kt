package com.appn.core_common.loading

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.RuntimeException
import java.util.concurrent.ConcurrentHashMap


/**
 * @author sufulu
 * LoadingHelper轻量级无侵入式的状态view使耦合度降到最低：处理标题栏、状态栏、loading/error的状态view
 */
class LoadingHelper {
    private lateinit var mAdapter: Adapter
    private var adapterpool: ConcurrentHashMap<String, Adapter> = ConcurrentHashMap()

    companion object {
        private var mDefault: LoadingHelper? = null
        var DEBUG = false
        const val DEFAULT = "default"

        const val STATUS_LOADING = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_ERROR = 3
        const val STATUS_EMPTY = 4
        const val STATUS_EMPTY_TEMP = 5

        /**
         * 默认的LoadingHelper 一般用于全局
         */
        fun getDefault(): LoadingHelper? {
            if (mDefault == null) {
                synchronized(LoadingHelper::class) {
                    if (mDefault == null) {
                        mDefault = LoadingHelper()
                    }
                }
            }
            return mDefault
        }

        /**
         * 获取某个类型的adapter 进行设置
         */
        fun get(viewType: String): LoadingHelper? {
            val adapter = getDefault()?.getAdapter(viewType)
            if (adapter != null) {
                getDefault()?.mAdapter = adapter
            }
            return getDefault()
        }

        /**
         * 初始化adapter适配器，推荐在application中进行初始化
         */
        fun initDefault(adapter: Adapter, debug: Boolean): LoadingHelper {
            DEBUG = debug
            getDefault()?.mAdapter = adapter
            getDefault()?.adapterpool?.set(DEFAULT, adapter)
            return getDefault()!!
        }
    }

    /**
     * 注册adapter,用于扩展 缓存多种adapter进行动态切换
     * 推荐再application中初始化设置
     */
    fun register(viewType: String, adapter: Adapter): LoadingHelper {
        adapterpool[viewType] = adapter
        return this
    }

    /**
     * 获取adapter
     */
    fun getAdapter(viewType: String): Adapter? {
        return adapterpool[viewType]
    }

    /**
     * 设置使用那个adapter,在当前页面动态更改adapter
     * @param viewType adapter的类型
     * @param adapter 首先会判断key不存在，如果不为空 设置缓存adapter
     */
    fun setAdapter(
        viewType: String,
        adapter: Adapter? = null,
        holder: Holder? = null
    ) {
        if (this.adapterpool.containsKey(viewType)) {
            if (adapter != null) {//更新adapter缓存配置
                this.adapterpool[viewType] = adapter
                this.mAdapter = adapter
            } else {
                this.mAdapter = this.adapterpool[viewType]!!
            }
        } else {
            if (adapter != null) {
                this.adapterpool[viewType] = adapter
                this.mAdapter = adapter
            }
        }
        holder?.notifyAdapter(this.mAdapter)
    }

    /**
     * status view wrap进行适配,添加到activity的顶级布局
     * loading status view 添加到activity的android.R.id.content 顶级布局中
     * @param activity 目标为某个activity
     */
    fun wrap(activity: Activity): Holder {
        val wrapper = activity.findViewById<ViewGroup>(android.R.id.content)
        return Holder(mAdapter, activity, wrapper)
    }

    /**
     * status view wrap 可以进行适配，将目标view添加到FL中
     * loading status view 添加到具体的某个view中
     * 注意wrap不要频繁调用，因为他会将view进行移动，在activity/fragment中调用一次即可，记录Holder 通过holder改变状态
     * @param view 目标为某个view
     */
    fun wrap(view: View): Holder {
        val wrapper: ViewGroup = FrameLayout(view.context)
        val lp = view.layoutParams
        if (lp != null) {
            wrapper.layoutParams = lp
        }
        if (view.parent != null) {
            val parent = view.parent as ViewGroup
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            parent.addView(wrapper, index)
        }
        val newLp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        wrapper.addView(view, newLp)
        Log.e("LoadingHelper", "wrap: 自动适配布局:" + wrapper.childCount)
        return Holder(mAdapter, view.context, wrapper)
    }

    /**
     * 直接覆盖到目标view上，注意目标view最好是RL和FL
     * @param view 目标为view的父类
     */
    fun cover(view: View): Holder {
        val parent = view.parent
            ?: throw RuntimeException("view has no parent to show as cover!")
        val viewGroup = parent as ViewGroup
        val wrapper = FrameLayout(view.context)
        viewGroup.addView(wrapper, view.layoutParams)
        return Holder(mAdapter, view.context, wrapper)
    }
}