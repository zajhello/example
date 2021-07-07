package com.dxmovie.dxbase.utils

import android.annotation.SuppressLint
import android.content.Context

object Utils {
    @SuppressLint("StaticFieldLeak")
    private var context: Context? = null

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    @JvmStatic
    fun init(context: Context) {
        Utils.context = context.applicationContext
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    @JvmStatic
    fun getContext(): Context? {
        if (context != null) {
            return context
        }
        throw NullPointerException("should be initialized in application")
    }

}