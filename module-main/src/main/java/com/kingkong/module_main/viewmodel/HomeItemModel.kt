package com.kingkong.module_main.viewmodel

import android.os.Handler
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.dxmovie.dxbase.viewmodel.BaseViewModel
import com.dxmovie.dxbase.widget.StatusViewLayout
import com.kingkong.module_main.adapter.HomeItemAdapter
import com.kingkong.module_main.adapter.HomeItemBean
import com.kingkong.module_main.adapter.HomeItemType

class HomeItemModel : BaseViewModel() {

    var statusObservable: ObservableInt = ObservableInt()
    var isAutoLoadMore = ObservableBoolean()
    var enable = ObservableBoolean()
    var isAutoRefresh = ObservableBoolean(false)
    var canLoadMore = ObservableBoolean(true)
    var itemAdapter: HomeItemAdapter? = null

    var time = 0
    override fun onCreate() {
        super.onCreate()
        statusObservable.set(StatusViewLayout.STATUS_LOADING)
        onRefresh()
    }

    var loadmoreCommand: BindingCommand = BindingCommand {
        onLoadMore()
    }

    private fun onLoadMore() {
        if (++time > 2) {
            isAutoLoadMore.set(false)
            canLoadMore.set(false)
            return
        }

        isAutoLoadMore.set(true)
        Handler().postDelayed({
            var bean1 = HomeItemBean()
            var bean2 = HomeItemBean()
            var bean3 = HomeItemBean()
            var bean4 = HomeItemBean()
            var bean5 = HomeItemBean()
            bean1.type = HomeItemType.MOVIE.value
            bean2.type = HomeItemType.ADS.value
            bean3.type = HomeItemType.HORIZONTAL.value
            bean4.type = HomeItemType.MOVIE.value
            bean5.type = HomeItemType.ADS.value
            itemAdapter!!.addAll(listOf(bean1, bean2, bean3, bean4, bean5))
            isAutoLoadMore.set(false)

        }, 1000)
    }

    var refreshCommand: BindingCommand = BindingCommand {
        time = 0
        canLoadMore.set(true)
        isAutoRefresh.set(true)
        onRefresh()
    }

    private fun onRefresh() {
        Handler().postDelayed({
            var bean1 = HomeItemBean()
            var bean2 = HomeItemBean()
            var bean3 = HomeItemBean()
            var bean4 = HomeItemBean()
            var bean5 = HomeItemBean()
            bean1.type = HomeItemType.BANNER.value
            bean2.type = HomeItemType.ADS.value
            bean3.type = HomeItemType.HORIZONTAL.value
            bean4.type = HomeItemType.MOVIE.value
            bean5.type = HomeItemType.HORIZONTAL.value
            itemAdapter!!.list = listOf(bean1, bean2, bean3, bean4, bean5)
            isAutoRefresh.set(false)
            enable.set(true)

            statusObservable.set(StatusViewLayout.STATUS_CONTENT)
        }, 1000)
    }


    init {
        itemAdapter = HomeItemAdapter()
    }
}