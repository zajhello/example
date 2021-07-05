package com.dxmovie.dxbase.dialog

import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import com.dxmovie.dxbase.activity.BaseActivity
import com.dxmovie.dxbase.utils.LoadingDialogHelper
import com.dxmovie.dxbase.utils.extention.no
import com.dxmovie.dxbase.widget.LoadingDialog

/**
 *@desc 基础DialogFragment (主要适用于需要在dialog中做网络请求)
 *      基础的dialog（只是作为展示，不需要额外复杂的操作）-->UI库中的[MaterialDialog]
 */
open class BaseDialogFragment : DialogFragment() {

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





    fun setCanceledOnTouchOutside(cancel: Boolean) {

    }


    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        (activity is BaseActivity).no {
            //如果不是BaseActivity 就调用清理，是就不调用，因为在BaseActivity中有调用
            LoadingDialogHelper.instance.clearLoading()
        }
    }

}