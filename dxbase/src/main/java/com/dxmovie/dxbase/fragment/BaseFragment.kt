package com.dxmovie.dxbase.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.dxmovie.dxbase.activity.BaseActivity
import com.dxmovie.dxbase.utils.LoadingDialogHelper
import com.dxmovie.dxbase.utils.extention.no
import com.dxmovie.dxbase.widget.LoadingDialog

/**
 * @description 所有fragment 的基类，作为一个规范，
 * 这里只处理了，androidx新的懒加载的逻辑
 *
 */
open class BaseFragment : Fragment() {

    var isLoaded = false
    var rootView: View? = null

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            lazyLoad()
            isLoaded = true
        }
        // 有的需要后续可见的时候
        if (isLoaded) {
            afterLazyLoad()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
        // 防止事件穿透
        view.isClickable = true
        super.onViewCreated(view, savedInstanceState)
    }


    /**
     * 显示dialog
     */
    open fun showLoadingDialog(): LoadingDialog? {
        return LoadingDialogHelper.instance.showLoading(activity)
    }

    /**
     * 隐藏dialog
     *
     */
    open fun hideLoadingDialog() {
        LoadingDialogHelper.instance.hideLoading(activity)
    }


    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        rootView = null
        (activity is BaseActivity).no {
            //如果不是BaseActivity 就调用清理，是就不调用，因为在BaseActivity中有调用
            LoadingDialogHelper.instance.clearLoading()
        }
    }

    /**
     * 第一次可见，调用，一般用于懒加载
     */
    open fun lazyLoad() {

    }

    /**
     * 在第一次懒加载之后，如果切换resumue 可见需要刷新数据的情况
     */
    open fun afterLazyLoad() {

    }
}
