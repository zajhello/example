package com.dxmovie.dxbase.binding.viewadapter.SmartRefreshLayou;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;

import com.dxmovie.dxbase.binding.command.BindingCommand;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 *
 */
public final class ViewAdapter {

    @BindingAdapter({"refreshCommand"})
    public static void setRefreshing(SmartRefreshLayout smartRefreshLayout, BindingCommand bindingCommand) {
        if (smartRefreshLayout != null)
            smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
                if (bindingCommand != null) {
                    bindingCommand.executeAction();
                }
            });
    }

    @BindingAdapter({"loadMoreCommand"})
    public static void setLoadMoreing(SmartRefreshLayout smartRefreshLayout, BindingCommand bindingCommand) {
        if (smartRefreshLayout != null)
            smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
                if (bindingCommand != null) {
                    bindingCommand.executeAction();
                }
            });
    }

//    @BindingAdapter({"noMoreData"})
//    public static void noMoreData(SmartRefreshLayout smartRefreshLayout, boolean isNoMoreData) {
//        if (smartRefreshLayout != null) {
//            smartRefreshLayout.setNoMoreData(isNoMoreData);
//        }
//    }


    @BindingAdapter(value = {"enable",}, requireAll = false)
    public static void enable(SmartRefreshLayout smartRefreshLayout, boolean enable) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableRefresh(enable);
            smartRefreshLayout.setEnableLoadMore(enable);
        }
    }

    @BindingAdapter(value = {"autoLoadMore", "canLoadMore", "delayed"}, requireAll = false)
    public static void finishLoadMore(SmartRefreshLayout smartRefreshLayout, boolean autoLoadMore, boolean canLoadMore, int delayed) {

        if (autoLoadMore) {
            smartRefreshLayout.autoLoadMore();
        } else {

            if (canLoadMore) {
                smartRefreshLayout.finishLoadMore(delayed);
            } else {
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }


    @BindingAdapter({"autoRefresh"})
    public static void autoRefresh(SmartRefreshLayout smartRefreshLayout, boolean isAuto) {
        if (smartRefreshLayout != null) {
            if (isAuto) {
                smartRefreshLayout.autoRefresh();
            } else {
                smartRefreshLayout.finishRefresh();
            }
        }
    }
}
