package com.dxmovie.dxbase.binding.viewadapter.scrollview

import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams

class ViewAdapter {

    companion object{
        @BindingAdapter("onScrollChangeCommand")
        fun onScrollChangeCommand(nestedScrollView: NestedScrollView, onScrollChangeCommand: BindingCommandWithParams<NestScrollDataWrapper?>?) {
            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> onScrollChangeCommand?.executeAction(NestScrollDataWrapper(scrollX, scrollY, oldScrollX, oldScrollY)) })
        }

        @BindingAdapter("onScrollChangeCommand")
        fun onScrollChangeCommand(scrollView: ScrollView, onScrollChangeCommand: BindingCommandWithParams<ViewAdapter.ScrollDataWrapper?>?) {
            scrollView.viewTreeObserver.addOnScrollChangedListener { onScrollChangeCommand?.executeAction(ViewAdapter.ScrollDataWrapper(scrollView.scrollX.toFloat(), scrollView.scrollY.toFloat())) }
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float)

    class NestScrollDataWrapper(var scrollX: Int, var scrollY: Int, var oldScrollX: Int, var oldScrollY: Int)
}