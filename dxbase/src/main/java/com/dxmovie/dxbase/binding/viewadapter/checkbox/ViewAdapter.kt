package com.dxmovie.dxbase.binding.viewadapter.checkbox

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams

class ViewAdapter {

    companion object {
        /**
         * @param bindingCommand //绑定监听
         */
        @JvmStatic
        @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
        fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommandWithParams<Boolean?>) {
            checkBox.setOnCheckedChangeListener { compoundButton, b -> bindingCommand.executeAction(b) }
        }
    }

}