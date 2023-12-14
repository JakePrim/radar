package com.appn.core_common.loading

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.appn.core_common.loading.LoadingHelper.Companion.DEBUG
import com.appn.core_common.loading.LoadingHelper.Companion.STATUS_EMPTY
import com.appn.core_common.loading.LoadingHelper.Companion.STATUS_ERROR
import com.appn.core_common.loading.LoadingHelper.Companion.STATUS_LOADING
import com.appn.core_common.loading.LoadingHelper.Companion.STATUS_SUCCESS
import java.lang.Exception
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL

/**
 * Holder 持有adapter进行具体的控制
 */
class Holder(var adapter: Adapter, val context: Context, val wrapper: ViewGroup) : HolderAction {

    var mTask: HRunnable? = null

    var mData: Any? = null

    var mToolbarData: Any? = null

    var mBackCallback: BackCallback? = null

    var mFunctionCallback: FunctionCallback? = null

    /**
     * 当前的状态
     */
    var curStatus: Int = -1

    /**
     * 当前的状态view
     */
    var curStatusView: View? = null

    /**
     * 当前的标题栏view
     */
    var curToolbarView: View? = null

    /**
     * 缓存状态view，下次直接取出即可
     */
    var mStatusViews = SparseArray<View>(4)

    /**
     * Toolbar view
     */
    var mToolbarWrapper: ViewGroup? = null

    /**
     * 获取toolBarData
     */
    fun <T> getToolbarData(): T? {
        try {
            return mToolbarData as T
        } catch (e: Exception) {
            if (DEBUG) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 获取额外的数据供Adapter使用
     */
    fun <T> getData(): T? {
        try {
            return mData as T
        } catch (e: Exception) {
            if (LoadingHelper.DEBUG) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 这个方法是在loading失败或者重试点击的时候的任务task
     */
    fun withRetry(task: HRunnable): Holder {
        this.mTask = task
        return this
    }

    /**
     * 一些额外的数据
     */
    fun withData(data: Any): Holder {
        this.mData = data
        return this
    }


    override fun showLoading() {
        showStatus(STATUS_LOADING)
    }

    override fun showError() {
        showStatus(STATUS_ERROR)
    }

    override fun showSuccess() {
        showStatus(STATUS_SUCCESS)
    }

    override fun showEmpty() {
        showStatus(STATUS_EMPTY)
    }

    /**
     * 更新adapter重置状态
     */
    fun notifyAdapter(adapter: Adapter): Holder {
        if (curStatusView != null) {
            wrapper.removeView(curStatusView)
        }
        curStatusView = null
        mStatusViews.clear()
        this.adapter = adapter
//        onBindStatusBar(bgColorRes, textColor)
//        onBindToolbar(enable, title, markerView, isCover)
        showStatus(curStatus)
        return this
    }

    /**
     * 可用于显示常见的四个状态，也可用于显示自定义的状态
     */
    override fun showStatus(status: Int) {
        if (curStatus == status) return
        curStatus = status
        //1. 首先从缓存中获取
        var convertView = mStatusViews[status]
        if (convertView == null) {
            convertView = curStatusView
        }
        try {
            //2. 判断view的位置及引用
            val view = adapter.getStatusView(this, convertView, status) ?: return
            if (view !== curStatusView || wrapper.indexOfChild(view) < 0) {
                if (curStatusView != null) {
                    wrapper.removeView(curStatusView)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //将状态view的权重为提升到最高等级 会在任何view的最上层
                    view.elevation = Float.MAX_VALUE
                }
                wrapper.addView(view)
                val lp = view.layoutParams
                if (lp != null) {
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            } else if (wrapper.indexOfChild(view) !== wrapper.childCount - 1) {
                // make sure loading status view at the front
                view.bringToFront()
            }
            curStatusView = view
            mStatusViews.put(status, view)
            printLog("adapter:${wrapper.childCount}")
        } catch (e: Exception) {
            printLog("exception:" + e.message)
            if (DEBUG) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置toolBar上的数据
     */
    fun notifyToolbar(toolBarData: Any): Holder {
        this.mToolbarData = toolBarData
        this.adapter.onBindToolBar(toolBarData)
        return this
    }

    /**
     * 设置toolbar的回调
     */
    fun withToolbarFunction(
        backCallback: BackCallback,
        functionCallback: FunctionCallback
    ): Holder {
        this.mBackCallback = backCallback
        this.mFunctionCallback = functionCallback
        return this
    }

    var enable: Boolean = false
    var title: String = ""
    var markerView: View? = null
    var isCover: Boolean = false

    /**
     * 绑定toolbar 标题栏,如果特殊的标题栏 带有联动效果 则无法使用，后期可以添加相关的功能
     * @param enable true 启用，FALSE 不启用(默认)
     * @param markerView toolBar要添加到到某个view的顶部，如果为null(默认)则添加到wrapper的顶部,如果不为空则添加到该view上
     * @param isCover 是否覆盖 TRUE 会覆盖到目标view的上方并且在顶部，FALSE(默认) 和目标纵向布局 在目标view的上方
     */
    override fun onBindToolbar(
        enable: Boolean,
        title: String,
        markerView: View?,
        isCover: Boolean
    ): Holder {
        this.enable = enable
        this.title = title
        this.markerView = markerView
        this.isCover = isCover
        if (enable) {
            //wrapper中有添加过mToolbarWrapper 则移除
            if (mToolbarWrapper != null) {
                wrapper.removeView(mToolbarWrapper)
            }
            if (isCover) {
                //层叠布局
                mToolbarWrapper = FrameLayout(context)
            } else {
                //纵向布局
                mToolbarWrapper = LinearLayout(context)
                (mToolbarWrapper as LinearLayout).orientation = VERTICAL
            }
            printLog("${wrapper.childCount}")
            val view = adapter.getToolBarView(this, title, curToolbarView)
            //如果之前有添加过 则移除掉
            if (curToolbarView != null) {
                mToolbarWrapper?.removeView(curToolbarView)
            }
            if (markerView != null && markerView != wrapper.getChildAt(0)) {
                //添加的标题栏 并没有和状态view是一个目标view
                val lp = markerView.layoutParams
                if (lp != null) {
                    mToolbarWrapper?.layoutParams = lp
                }
                if (markerView.parent != null) {
                    val parent = markerView.parent as ViewGroup
                    val index = parent.indexOfChild(markerView)
                    parent.removeView(markerView)
                    parent.addView(mToolbarWrapper, index)
                }
                val newLp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                if (isCover) {
                    mToolbarWrapper?.addView(markerView, newLp)
                    mToolbarWrapper?.addView(view) //标题栏盖到 target view上
                } else {
                    mToolbarWrapper?.addView(view)
                    mToolbarWrapper?.addView(markerView, newLp)//target在toolbar下面
                }
            } else {
                //获取到目标view
                val target = wrapper.getChildAt(0)
                //设置layoutParams
                val lp = target.layoutParams
                if (lp != null) {
                    mToolbarWrapper?.layoutParams = lp
                }
                //移除目标view
                wrapper.removeViewAt(0)
                //将mToolbarWrapper 添加到wrapper中，不用管状态view因为状态view设置的是最高优先级
                wrapper.addView(mToolbarWrapper, 0)
                //将目标view添加
                val newLp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                if (isCover) {
                    mToolbarWrapper?.addView(target, newLp)
                    mToolbarWrapper?.addView(view) //标题栏盖到 target view上
                } else {
                    mToolbarWrapper?.addView(view)
                    mToolbarWrapper?.addView(target, newLp)//target在toolbar下面
                }
            }
            //设置标题栏的layoutParams，注意标题栏的高度不能MATCH_PARENT
            val lp2 = view?.layoutParams
            if (lp2 != null) {
                lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
                lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            //记录
            curToolbarView = view
        } else {
            //移除
            if (mToolbarWrapper != null) {
                if (curToolbarView != null) {
                    mToolbarWrapper?.removeView(curToolbarView)
                }
//                wrapper.removeView(mToolbarWrapper)
            }
        }
        return this
    }

    override fun hideToolbar(): Holder {
        if (curToolbarView != null) {
            curToolbarView?.visibility = GONE
        }
        return this
    }

    override fun showToolbar(): Holder {
        if (curToolbarView != null) {
            curToolbarView?.visibility = VISIBLE
        }
        return this
    }

    private fun printLog(msg: String) {
        if (DEBUG) {
            Log.e("LoadingHelper", msg)
        }
    }

}