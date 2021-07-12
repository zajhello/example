package com.dxmovie.dxbase.binding.viewadapter.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams
import com.dxmovie.dxbase.binding.viewadapter.recyclerview.LineManagers.LineManagerFactory
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ViewAdapter {
    companion object {

        @JvmStatic
        @BindingAdapter("lineManager")
        fun setLineManager(recyclerView: RecyclerView, lineManagerFactory: LineManagerFactory) {
            recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView)!!)
        }

        @JvmStatic
        @BindingAdapter("layoutManagers")
        fun setLayoutManager(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager?) {
            recyclerView.layoutManager = layoutManager
        }

        @JvmStatic
        @BindingAdapter(value = ["onScrollChangeCommand", "onScrollStateChangedCommand"], requireAll = false)
        fun onScrollChangeCommand(recyclerView: RecyclerView,
                                  onScrollChangeCommand: BindingCommandWithParams<ViewAdapter.ScrollDataWrapper?>?,
                                  onScrollStateChangedCommand: BindingCommandWithParams<Int?>?) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var state = 0
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    onScrollChangeCommand?.executeAction(ViewAdapter.ScrollDataWrapper(dx.toFloat(), dy.toFloat(), state))
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    state = newState
                    onScrollStateChangedCommand?.executeAction(newState)
                }
            })
        }

        @JvmStatic
        @BindingAdapter("onLoadMoreCommand")
        fun onLoadMoreCommand(recyclerView: RecyclerView, onLoadMoreCommand: BindingCommandWithParams<Int?>?) {
            val listener: RecyclerView.OnScrollListener = OnScrollListener(onLoadMoreCommand)
            recyclerView.addOnScrollListener(listener)
        }

        @JvmStatic
        @BindingAdapter("itemAnimator")
        fun setItemAnimator(recyclerView: RecyclerView, animator: ItemAnimator?) {
            recyclerView.itemAnimator = animator
        }
    }

    class OnScrollListener(onLoadMoreCommand: BindingCommandWithParams<Int?>?) : RecyclerView.OnScrollListener() {
        private val methodInvoke = PublishSubject.create<Int>()
        private val onLoadMoreCommand: BindingCommandWithParams<Int?>? = onLoadMoreCommand
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.adapter!!.itemCount)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        init {
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe { integer -> onLoadMoreCommand?.executeAction(integer) }
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)

}