package com.dxmovie.dxbase.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dxmovie.dxbase.R
import com.dxmovie.dxbase.utils.extention.otherwise
import com.dxmovie.dxbase.utils.extention.yes
import com.dxmovie.dxbase.viewmodel.BaseViewModel

/**
 *@desc 写了一些模板方法，方便使用
 *  假设不想使用可以直接使用[DXBaseDialogFragment]或者[BaseDialogFragment]
 */
abstract class DXDialogFragment<DB : ViewDataBinding> : DXBaseDialogFragment() {

    var binding: DB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(setDialogStyle(), setDialogTheme())
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isFullScreen().yes {
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog?.window?.statusBarColor = Color.TRANSPARENT
            dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, contentViewId(), null, false)
        binding?.setVariable(getViewModelId(), getBindViewModel())
        return binding?.root
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes.gravity = setGravity()
        }
        initView()
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    open fun <T : BaseViewModel?> createViewModel(cls: Class<T>?): T {
        return ViewModelProvider(this)[cls!!]
    }

    /***
     * dialog的类型
     * @return [STYLE_NORMAL], [STYLE_NO_TITLE], [STYLE_NO_TITLE], [STYLE_NO_TITLE]
     */
    open fun setDialogStyle(): Int = STYLE_NO_TITLE

    /** dialog 主题
     * @return theme dialog (最好继承dialog_base) */
    open fun setDialogTheme(): Int = R.style.dialog_base

    /** 是否全屏
     * @return default=false */
    open fun isFullScreen(): Boolean = true


    /** 设置位置
     * [Gravity.CENTER] [Gravity.BOTTOM],[Gravity.TOP],[Gravity.RIGHT]...
     * 如果 isFullScreen()=true  则失效*/
    open fun setGravity(): Int = Gravity.CENTER


    /** 布局ID */
    abstract fun contentViewId(): Int

    /** viewModel ID */
    abstract fun getViewModelId(): Int

    /** 获取ViewModel */
    abstract fun getBindViewModel(): ViewModel

    /** 初始化的一些操作
     * 设置宽度、高度等等。。。初始化一系列 */
    abstract fun initView();

}