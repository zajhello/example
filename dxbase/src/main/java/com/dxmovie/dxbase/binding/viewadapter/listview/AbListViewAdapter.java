package com.dxmovie.dxbase.binding.viewadapter.listview;

import android.annotation.SuppressLint;

import androidx.databinding.BindingAdapter;

import com.dxmovie.dxbase.binding.command.BindingCommand;
import com.dxmovie.dxbase.widget.StatusViewLayout;

public class AbListViewAdapter {

    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"sv_status"}, requireAll = false)
    public static void onLongClickCommand(StatusViewLayout statusViewLayout, int status) {
        statusViewLayout.setStatus(status);
    }

    @BindingAdapter(value = "sl_retry")
    public static void setOnRetryLisener(StatusViewLayout statusViewLayout, BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            statusViewLayout.setOnRetryListener(v -> bindingCommand.executeAction());
            statusViewLayout.setEmptyListener(v -> bindingCommand.executeAction());
        }
    }

    @BindingAdapter(value = "sv_retry")
    public static void setOnErrorRetryLisener(StatusViewLayout statusViewLayout, BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            statusViewLayout.setOnRetryListener(v -> bindingCommand.executeAction());
        }
    }
}
