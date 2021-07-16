package com.kingkong.common_library.utils

import android.view.View
import android.view.ViewGroup

object UiUtils {
    /**
     * 清空view的父容器，使得view被添加到其它的容器中不会发生错误
     */
    fun clearParent(view: View?) {
        if (view == null) {
            return
        }
        val parent = view.parent
        if (parent == null || parent !is ViewGroup) {
            return
        }
        // 有些机型 把子 view 移除后还保存这布局参数，这样会导致这个 view 在其它的容器中出现布局匹配错误
        parent.removeView(view)
    }

    /**
     * 设置View的点击事件，根据是否为空设置View的点击性
     */
    fun setViewClickListener(view: View, onClickListener: View.OnClickListener?) {
        view.setOnClickListener(onClickListener)
        view.isClickable = onClickListener != null
    }
}