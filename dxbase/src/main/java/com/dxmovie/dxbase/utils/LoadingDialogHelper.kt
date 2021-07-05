package com.dxmovie.dxbase.utils

import android.annotation.SuppressLint
import android.app.Activity
import com.dxmovie.dxbase.widget.LoadingDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 为了方便统一调用loading 类
 */
class LoadingDialogHelper private constructor() {
    companion object {
        val instance: LoadingDialogHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LoadingDialogHelper()
        }
    }

    private val loadingMap = HashMap<Any, LoadingDialog>()
    private val lock = Any()

    /**
     * tag :如果要在一个activity中同时显示多个，请传不同的tag
     * activity: 当没有TAG时，activity为默认的tag这样一个activity中同时只会有一个loadingDialog
     * animateSrc: dialog的动画文件
     *
     */
    fun showLoading(tag: Any?, activity: Activity?): LoadingDialog? {
        synchronized(lock) {
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                return null
            }
            val mtag = tag?.hashCode() ?: activity.hashCode()
            if (loadingMap[mtag] == null) {
                val loadingDialog = LoadingDialog(activity)
                loadingMap[mtag] = loadingDialog
            }
            return loadingMap[mtag]?.apply {
                show()
            }
        }
    }


    /**
     * 显示 dialog
     */
    fun showLoading(activity: Activity?): LoadingDialog? {
        return showLoading(null, activity)
    }

    /***
     * tag 如果显示没有设置， 请传 activity对像
     * 关闭 dialog
     */
    @SuppressLint("CheckResult")
    fun hideLoading(tag: Any?) {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    tag?.let {
                        synchronized(lock) {
                            loadingMap[tag.hashCode()]?.apply {
                                dismiss()
                            }
                        }

                    }

                }
    }

    /**
     * 在activity onDestroy 中调用
     * 清楚当前activity中使用的loadingDialog
     */
    fun clearLoading() {
        synchronized(lock) {
            val iterator: MutableIterator<Map.Entry<Any, LoadingDialog>> = loadingMap.entries.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                try {
                    item.value?.let { ld ->
                        if (ld.activity == null) {
                            ld.dismiss()
                            iterator.remove()
                        } else {
                            ld.activity?.let { activity ->
                                if (activity.isFinishing || activity.isDestroyed) {
                                    ld.dismiss()
                                    iterator.remove()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    iterator.remove()
                }
            }
        }

    }

}


