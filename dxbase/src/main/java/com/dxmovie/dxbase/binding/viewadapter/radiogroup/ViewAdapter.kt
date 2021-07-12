package com.dxmovie.dxbase.binding.viewadapter.radiogroup

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams

class ViewAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
        fun onCheckedChangedCommand(radioGroup: RadioGroup, bindingCommand: BindingCommandWithParams<String?>) {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val radioButton = group.findViewById<View>(checkedId) as RadioButton
                bindingCommand.executeAction(radioButton.text.toString())
            }
        }
    }
}