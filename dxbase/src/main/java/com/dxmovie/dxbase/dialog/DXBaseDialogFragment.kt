package com.dxmovie.dxbase.dialog

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dxmovie.dxbase.viewmodel.BaseViewModel

/**
 *@desc  和ViewModel绑定，赋予生命周期
 */
open class DXBaseDialogFragment : BaseDialogFragment() {

    internal var viewModel: ViewModel? = null

    @CallSuper
    open fun initViewModel(viewModel: ViewModel?) {
        bindLifecycle(viewModel)
    }

    /**
     * 如果需要，绑定生命周期
     */
    open fun bindLifecycle(viewModel: ViewModel?) {
        viewModel?.let {
            this.viewModel = viewModel
            if (it is LifecycleObserver) {
                lifecycle.addObserver(it)
            }
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            if (it is LifecycleObserver) {
                lifecycle.removeObserver(it)
            }
        }
    }

    /**
     * hook 下创建流程
     * 主要的目的是在viewModel创建完之后自动初始化
     * 否则 要么写模板方法，破坏了原来的代码结构，要么用户自己初始化，要写多余代码，增加学习和出错成本
     */
    private var hookFactory: DefaultFactoryWrapper? = null

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        val defaultViewModelProviderFactory = super.getDefaultViewModelProviderFactory()
        if (hookFactory == null) {
            hookFactory = DefaultFactoryWrapper(defaultViewModelProviderFactory)
            return hookFactory!!
        }
        return hookFactory!!
    }

    internal inner class DefaultFactoryWrapper(private var factory: ViewModelProvider.Factory) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = factory.create(modelClass)
            initViewModel(viewModel)
            return viewModel
        }
    }
}