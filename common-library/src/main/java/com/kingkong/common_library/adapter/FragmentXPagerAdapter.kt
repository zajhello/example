package com.kingkong.common_library.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*


class FragmentXPagerAdapter constructor(@NonNull fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList: List<Fragment> = ArrayList()

    constructor(fm: FragmentManager, fragmentList: List<Fragment>) : this(fm) {
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

}