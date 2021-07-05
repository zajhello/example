package com.kingkong.module_main.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dxmovie.dxbase.fragment.DXBaseFragment;
import com.kingkong.module_main.BR;
import com.kingkong.module_main.R;
import com.kingkong.module_main.databinding.MainFragmentHomeBinding;
import com.kingkong.module_main.databinding.MainTaskFragmentBinding;
import com.kingkong.module_main.viewmodel.HomeModel;
import com.kingkong.module_main.viewmodel.TaskModel;

import org.jetbrains.annotations.NotNull;

public class TaskFragment extends DXBaseFragment<MainTaskFragmentBinding, TaskModel> {

    public static TaskFragment newInstance() {
        TaskFragment mTask = new TaskFragment();
        return mTask;
    }

    @Override
    public int getContentViewLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.main_task_fragment;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public TaskModel getViewModel() {
        return createViewModel(TaskModel.class);
    }

    @Override
    public void onViewCreated(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {



    }
}