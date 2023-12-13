package com.appn.core_common.base.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.appn.core_common.integration.EventBusManager

class FragmentDelegateImpl(private val fm: FragmentManager, private val fragment: Fragment) :
    FragmentDelegate {

    private val mIFragment: IFragment = fragment as IFragment

    override fun onAttach(context: Context) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mIFragment.useEvent()) {
            EventBusManager.getInstance()?.register(fragment)
        }
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onDestroyView() {
    }

    override fun onDestroy() {
        if (mIFragment.useEvent()) {
            EventBusManager.getInstance()?.unregister(fragment)
        }
    }

    override fun onDetach() {
    }

    override fun isAdded(): Boolean {
        return fragment.isAdded
    }

}