package com.dxmovie.dxbase.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.dxmovie.dxbase.viewmodel.BaseViewModel;


/**
 * 封装VM的BaseActivity
 */
public abstract class DXBaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends XBaseActivity {

    public V binding;
    public VM viewModel;
    private int viewModelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化databinding和viewModel
        initViewDataBinding(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑dataBinding
        if (binding != null) {
            binding.unbind();
        }
    }


    public void initViewDataBinding(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getContentViewLayout(savedInstanceState));
        viewModelId = getViewModelId();
        viewModel = getViewModel();
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);

    }

    /**
     * 获取页面的布局文件
     *
     * @param savedInstanceState
     * @return 布局的id
     */
    public abstract int getContentViewLayout(Bundle savedInstanceState);

    /**
     * 获取ViewModel的id
     *
     * @return 变量的id
     */
    public abstract int getViewModelId();

    /**
     * 初始化ViewModel
     *
     * @return
     */
    public abstract VM getViewModel();

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends BaseViewModel> T createViewModel(Class<T> cls) {
        return new ViewModelProvider(this).get(cls);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
