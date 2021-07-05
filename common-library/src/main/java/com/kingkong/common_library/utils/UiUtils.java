package com.kingkong.common_library.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public final  class UiUtils {

    /**
     * 清空view的父容器，使得view被添加到其它的容器中不会发生错误
     */
    public static void clearParent(View view) {
        if(view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if(parent == null || !(parent instanceof ViewGroup)) {
            return;
        }
        // 有些机型 把子 view 移除后还保存这布局参数，这样会导致这个 view 在其它的容器中出现布局匹配错误
        ((ViewGroup)parent).removeView(view);
    }

    /**
     * 设置View的点击事件，根据是否为空设置View的点击性
     */
    public static void setViewClickListener(View view, View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
        view.setClickable(onClickListener != null);
    }

}
