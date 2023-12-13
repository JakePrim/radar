package com.appn.core_common.base.delegate

import android.os.Bundle
import android.view.LayoutInflater

interface IFragment {

    /**
     * 是否使用EventBus,自动注册EventBus [FragmentDelegate] 中实现
     */
    fun useEvent(): Boolean

    /**
     * 初始化View
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * setData 通过此方法可以使Fragment与外界做一些交互和通信
     *  Example usage:
     *      * public void setData(@Nullable Object data) {
     *      *     if (data != null && data instanceof Message) {
     *      *         switch (((Message) data).what) {
     *      *             case 0:
     *      *                 loadData(((Message) data).arg1);
     *      *                 break;
     *      *             case 1:
     *      *                 refreshUI();
     *      *                 break;
     *      *             default:
     *      *                 //do something
     *      *                 break;
     *      *         }
     *      *     }
     *      * }
     *      *
     *      * // call setData(Object):
     *      * Message data = new Message();
     *      * data.what = 0;
     *      * data.arg1 = 1;
     *      * fragment.setData(data);
     */
    fun setData(data:Any)
}