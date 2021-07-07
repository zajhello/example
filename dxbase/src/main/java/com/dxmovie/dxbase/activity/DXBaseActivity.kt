package com.dxmovie.dxbase.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dxmovie.dxbase.viewmodel.BaseViewModel
/**
 * 业务基类AC
 * MVVM 功能继承类
 */
abstract class DXBaseActivity<V : ViewDataBinding, VM : BaseViewModel> : XBaseActivity() {

    var binding: V? = null
    var vModel: VM? = null
    private var viewModelId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化databinding和viewModel
        initViewDataBinding(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        //解绑dataBinding
        binding?.unbind()
    }

    open fun initViewDataBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, getContentViewLayout(savedInstanceState))
        viewModelId = getViewModelId()
        vModel = getViewModel()
        //关联ViewModel
        binding!!.setVariable(viewModelId, vModel)
    }

    /**
     * 获取页面的布局文件
     *
     * @param savedInstanceState
     * @return 布局的id
     */
    abstract fun getContentViewLayout(savedInstanceState: Bundle?): Int

    /**
     * 获取ViewModel的id
     *
     * @return 变量的id
     */
    abstract fun getViewModelId(): Int

    /**
     * 初始化ViewModel
     *
     * @return
     */
    abstract fun getViewModel(): VM?

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

}