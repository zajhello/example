package com.dxmovie.dxbase.binding.viewadapter.SmartRefreshLayou

import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout

class ViewAdapter {

    companion object{
        @JvmStatic
        @BindingAdapter("refreshCommand")
        fun setRefreshing(smartRefreshLayout: SmartRefreshLayout?, bindingCommand: BindingCommand?) {
            smartRefreshLayout?.setOnRefreshListener { refreshLayout: RefreshLayout? ->
                bindingCommand?.executeAction()
            }
        }

        @JvmStatic
        @BindingAdapter("loadMoreCommand")
        fun setLoadMoreing(smartRefreshLayout: SmartRefreshLayout?, bindingCommand: BindingCommand?) {
            smartRefreshLayout?.setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
                bindingCommand?.executeAction()
            }
        }

//    @BindingAdapter({"noMoreData"})
//    public static void noMoreData(SmartRefreshLayout smartRefreshLayout, boolean isNoMoreData) {
//        if (smartRefreshLayout != null) {
//            smartRefreshLayout.setNoMoreData(isNoMoreData);
//        }
//    }


        //    @BindingAdapter({"noMoreData"})
//    public static void noMoreData(SmartRefreshLayout smartRefreshLayout, boolean isNoMoreData) {
//        if (smartRefreshLayout != null) {
//            smartRefreshLayout.setNoMoreData(isNoMoreData);
//        }
//    }
        @JvmStatic
        @BindingAdapter(value = ["enable"], requireAll = false)
        fun enable(smartRefreshLayout: SmartRefreshLayout?, enable: Boolean) {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.setEnableRefresh(enable)
                smartRefreshLayout.setEnableLoadMore(enable)
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["autoLoadMore", "canLoadMore", "delayed"], requireAll = false)
        fun finishLoadMore(smartRefreshLayout: SmartRefreshLayout, autoLoadMore: Boolean, canLoadMore: Boolean, delayed: Int) {
            if (autoLoadMore) {
                smartRefreshLayout.autoLoadMore()
            } else {
                if (canLoadMore) {
                    smartRefreshLayout.finishLoadMore(delayed)
                } else {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }


        @JvmStatic
        @BindingAdapter("autoRefresh")
        fun autoRefresh(smartRefreshLayout: SmartRefreshLayout?, isAuto: Boolean) {
            if (smartRefreshLayout != null) {
                if (isAuto) {
                    smartRefreshLayout.autoRefresh()
                } else {
                    smartRefreshLayout.finishRefresh()
                }
            }
        }
    }
}