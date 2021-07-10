package com.dxmovie.dxbase.binding.viewadapter.listview

import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ViewAdapter {

    companion object{
        @JvmStatic
        @BindingAdapter(value = ["onScrollChangeCommand", "onScrollStateChangedCommand"], requireAll = false)
        fun onScrollChangeCommand(listView: ListView,
                                  onScrollChangeCommand: BindingCommandWithParams<ListViewScrollDataWrapper?>?,
                                  onScrollStateChangedCommand: BindingCommandWithParams<Int?>?) {
            listView.setOnScrollListener(object : AbsListView.OnScrollListener {
                private var scrollState = 0
                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                    this.scrollState = scrollState
                    onScrollStateChangedCommand?.executeAction(scrollState)
                }

                override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    onScrollChangeCommand?.executeAction(ListViewScrollDataWrapper(scrollState, firstVisibleItem, visibleItemCount, totalItemCount))
                }
            })
        }


        @JvmStatic
        @BindingAdapter(value = ["onItemClickCommand"], requireAll = false)
        fun onItemClickCommand(listView: ListView, onItemClickCommand: BindingCommandWithParams<Int?>?) {
            listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> onItemClickCommand?.executeAction(position) }
        }


        @JvmStatic
        @BindingAdapter("onLoadMoreCommand")
        fun onLoadMoreCommand(listView: ListView, onLoadMoreCommand: BindingCommandWithParams<Int?>) {
            listView.setOnScrollListener(OnScrollListener(listView, onLoadMoreCommand))
        }

        class OnScrollListener(listView: ListView, onLoadMoreCommand: BindingCommandWithParams<Int?>) : AbsListView.OnScrollListener {
            private val methodInvoke = PublishSubject.create<Int>()
            private val onLoadMoreCommand: BindingCommandWithParams<Int?>?
            private val listView: ListView
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount != 0 && (totalItemCount != listView.headerViewsCount
                                + listView.footerViewsCount)) {
                    if (onLoadMoreCommand != null) {
                        methodInvoke.onNext(totalItemCount)
                    }
                }
            }

            init {
                this.onLoadMoreCommand = onLoadMoreCommand
                this.listView = listView
                methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe { integer -> onLoadMoreCommand.executeAction(integer) }
            }
        }

        class ListViewScrollDataWrapper(var scrollState: Int, var firstVisibleItem: Int, var visibleItemCount: Int, var totalItemCount: Int)
    }
}