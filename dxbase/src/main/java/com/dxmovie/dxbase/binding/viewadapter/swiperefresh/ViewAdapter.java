package com.dxmovie.dxbase.binding.viewadapter.swiperefresh;


import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dxmovie.dxbase.binding.command.BindingCommand;


/**
 *
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final BindingCommand onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshCommand != null) {
                    onRefreshCommand.executeAction();
                }
            }
        });
    }

    /**  是否可以刷新 */
    @BindingAdapter("isCanRefresh")
    public static void isSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout, Boolean isRefresh) {
        swipeRefreshLayout.setEnabled(isRefresh);
    }

    /**
     * 设置正在刷新
     */
    @BindingAdapter("isRefreshing")
    public static void refreshing(SwipeRefreshLayout swipeRefreshLayout, Boolean isRefresh) {
        swipeRefreshLayout.setRefreshing(isRefresh);
    }

}
