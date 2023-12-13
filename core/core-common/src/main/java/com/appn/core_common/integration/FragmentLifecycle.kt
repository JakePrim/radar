package com.appn.core_common.integration

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.appn.core_common.base.delegate.ActivityDelegate
import com.appn.core_common.base.delegate.ActivityDelegateImpl
import com.appn.core_common.base.delegate.FragmentDelegate
import com.appn.core_common.base.delegate.FragmentDelegateImpl
import com.appn.core_common.base.delegate.IActivity
import com.appn.core_common.base.delegate.IFragment

/**
 * FragmentLifecycle Fragment的生命周期类
 */
class FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {
    private var cacheFragmentDelegate = mutableMapOf<String, FragmentDelegate>()

    private fun fetchFragmentDelegate(fm: FragmentManager, fragment: Fragment): FragmentDelegate? {
        var fmDelegate: FragmentDelegate? = null
        if (fragment is IFragment) {
            fmDelegate = cacheFragmentDelegate[fragment.javaClass.name]
            if (fmDelegate == null) {
                fmDelegate = FragmentDelegateImpl(fm, fragment)
                cacheFragmentDelegate[fragment.javaClass.name] = fmDelegate
            }
        }
        return fmDelegate
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        fetchFragmentDelegate(fm, f)?.onAttach(context)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        fetchFragmentDelegate(fm, f)?.onCreate(savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        fetchFragmentDelegate(fm, f)?.onCreateView(v, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
        fetchFragmentDelegate(fm, f)?.onActivityCreate(savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        fetchFragmentDelegate(fm, f)?.onStart()
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        fetchFragmentDelegate(fm, f)?.onResume()
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        fetchFragmentDelegate(fm, f)?.onPause()
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        fetchFragmentDelegate(fm, f)?.onStop()
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        fetchFragmentDelegate(fm, f)?.onSaveInstanceState(outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        fetchFragmentDelegate(fm, f)?.onDestroyView()
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        fetchFragmentDelegate(fm, f)?.onDestroy()
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        fetchFragmentDelegate(fm, f)?.onDetach()
    }
}