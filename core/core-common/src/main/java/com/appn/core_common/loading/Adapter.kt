package com.appn.core_common.loading

import android.view.View
import androidx.annotation.ColorRes
import com.appn.core_common.loading.Holder

/**
 * 状态适配器
 */
interface Adapter {
    /**
     * 返回状态view
     * @param holder Holder处理显示状态view
     * @param convertView 状态view,表示缓存的view如果不为null并且和status的instanceOf一致则直接返回，不用在重新获取 提升性能
     * @param status 标记具体的状态值
     */
    fun getStatusView(holder: Holder, convertView: View?, status: Int): View?

    /**
     * view 是内容布局View，将toolbar添加到内容布局之上，或者覆盖内容布局的上面
     */
    fun getToolBarView(holder: Holder, title: String, toolBarView: View?): View?

    /**
     * toolbar 绑定数据
     */
    fun onBindToolBar(data: Any?)
}