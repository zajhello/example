package com.kingkong.common_library.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class ViewPager2Adapter : FragmentStateAdapter {

    private var fragmentList: List<Fragment> = ArrayList()

    constructor(fragment: Fragment, fragmentList: List<Fragment>) : super(fragment) {
        this.fragmentList = fragmentList
    }

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    constructor(fragment: Fragment) : super(fragment)

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle)

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

}