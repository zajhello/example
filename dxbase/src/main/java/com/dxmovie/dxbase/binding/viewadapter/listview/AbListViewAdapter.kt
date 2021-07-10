package com.dxmovie.dxbase.binding.viewadapter.listview

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.dxmovie.dxbase.widget.StatusViewLayout

class AbListViewAdapter {
    companion object{

        @JvmStatic
        @SuppressLint("CheckResult")
        @BindingAdapter(value = ["sv_status"], requireAll = false)
        fun onLongClickCommand(statusViewLayout: StatusViewLayout, status: Int) {
            statusViewLayout.setStatus(status)
        }

        @JvmStatic
        @BindingAdapter(value = ["sl_retry"])
        fun setOnRetryLisener(statusViewLayout: StatusViewLayout, bindingCommand: BindingCommand?) {
            if (bindingCommand != null) {
                statusViewLayout.setOnRetryListener(View.OnClickListener { v: View? -> bindingCommand.executeAction() })
                statusViewLayout.setEmptyListener(View.OnClickListener { v: View? -> bindingCommand.executeAction() })
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["sv_retry"])
        fun setOnErrorRetryLisener(statusViewLayout: StatusViewLayout, bindingCommand: BindingCommand?) {
            if (bindingCommand != null) {
                statusViewLayout.setOnRetryListener(View.OnClickListener { v: View? -> bindingCommand.executeAction() })
            }
        }
    }
}