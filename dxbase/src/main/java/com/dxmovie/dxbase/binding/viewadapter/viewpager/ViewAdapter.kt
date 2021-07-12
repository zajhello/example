package com.dxmovie.dxbase.binding.viewadapter.viewpager

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams

class ViewAdapter {

    companion object{
        @BindingAdapter(value = ["onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"], requireAll = false)
        fun onScrollChangeCommand(viewPager: ViewPager,
                                  onPageScrolledCommand: BindingCommandWithParams<ViewPagerDataWrapper?>?,
                                  onPageSelectedCommand: BindingCommandWithParams<Int?>?,
                                  onPageScrollStateChangedCommand: BindingCommandWithParams<Int?>?) {
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                private var state = 0
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    onPageScrolledCommand?.executeAction(ViewPagerDataWrapper(position.toFloat(), positionOffset, positionOffsetPixels, state))
                }

                override fun onPageSelected(position: Int) {
                    onPageSelectedCommand?.executeAction(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    this.state = state
                    onPageScrollStateChangedCommand?.executeAction(state)
                }
            })
        }
    }

    class ViewPagerDataWrapper(var position: Float, var positionOffset: Float, var positionOffsetPixels: Int, var state: Int)
}