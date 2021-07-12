package com.dxmovie.dxbase.binding.viewadapter.swiperefresh

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dxmovie.dxbase.binding.command.BindingCommand

class ViewAdapter {
    companion object {
        //下拉刷新命令
        @JvmStatic
        @BindingAdapter("onRefreshCommand")
        fun onRefreshCommand(swipeRefreshLayout: SwipeRefreshLayout, onRefreshCommand: BindingCommand?) {
            swipeRefreshLayout.setOnRefreshListener { onRefreshCommand?.executeAction() }
        }

        /**  是否可以刷新  */
        @JvmStatic
        @BindingAdapter("isCanRefresh")
        fun isSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout, isRefresh: Boolean?) {
            swipeRefreshLayout.isEnabled = isRefresh!!
        }

        /**
         * 设置正在刷新
         */
        @JvmStatic
        @BindingAdapter("isRefreshing")
        fun refreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefresh: Boolean?) {
            swipeRefreshLayout.isRefreshing = isRefresh!!
        }
    }

}