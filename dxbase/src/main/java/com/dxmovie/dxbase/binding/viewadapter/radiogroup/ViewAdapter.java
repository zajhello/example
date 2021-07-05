package com.dxmovie.dxbase.binding.viewadapter.radiogroup;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxmovie.dxbase.binding.command.BindingCommand;
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams;

/**
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommandWithParams<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                bindingCommand.executeAction(radioButton.getText().toString());
            }
        });
    }
}
