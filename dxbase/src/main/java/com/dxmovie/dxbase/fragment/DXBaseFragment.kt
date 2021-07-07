package com.dxmovie.dxbase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dxmovie.dxbase.viewmodel.BaseViewModel

abstract class DXBaseFragment<V : ViewDataBinding, VM : BaseViewModel> : XBaseFragment() {
    var binding: V? = null
    var vModel: VM? = null
    private var viewModelId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, getContentViewLayout(inflater, container, savedInstanceState), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化ViewModel dataBinding
        initViewDataBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
    }

    open fun initViewDataBinding() {
        vModel = getViewModel()
        viewModelId = getViewModelId()
        binding!!.setVariable(viewModelId, vModel)
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun getContentViewLayout(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun getViewModelId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    abstract fun getViewModel(): VM


    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    open fun <T : BaseViewModel?> createViewModel(cls: Class<T>?): T {
        return createViewModel(this, cls)
    }

    open fun <T : BaseViewModel?> createViewModel(owner: ViewModelStoreOwner?, cls: Class<T>?): T {
        return ViewModelProvider(owner!!)[cls!!]
    }

}