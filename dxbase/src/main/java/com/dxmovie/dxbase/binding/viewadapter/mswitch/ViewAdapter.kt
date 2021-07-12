package com.dxmovie.dxbase.binding.viewadapter.mswitch

import android.widget.Switch
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams

class ViewAdapter {
    companion object {


        /**
         * 设置开关状态
         *
         * @param mSwitch Switch控件
         */
        @JvmStatic
        @BindingAdapter("switchState")
        fun setSwitchState(mSwitch: Switch, isChecked: Boolean) {
            mSwitch.isChecked = isChecked
        }

        /**
         * Switch的状态改变监听
         *
         * @param mSwitch        Switch控件
         * @param changeListener 事件绑定命令
         */
        @JvmStatic
        @BindingAdapter("onCheckedChangeCommand")
        fun onCheckedChangeCommand(mSwitch: Switch, changeListener: BindingCommandWithParams<Boolean?>?) {
            if (changeListener != null) {
                mSwitch.setOnCheckedChangeListener { buttonView, isChecked -> changeListener.executeAction(isChecked) }
            }
        }
    }
}