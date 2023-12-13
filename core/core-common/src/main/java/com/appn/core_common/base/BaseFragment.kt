package com.appn.core_common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appn.core_common.base.delegate.IFragment

abstract class BaseFragment : Fragment(), IFragment {
    protected var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun useEvent(): Boolean {
        return true
    }

    override fun setData(data: Any) {

    }
}