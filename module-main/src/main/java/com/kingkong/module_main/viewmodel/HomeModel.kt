package com.kingkong.module_main.viewmodel

import androidx.databinding.ObservableBoolean
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.dxmovie.dxbase.utils.ToastUtils
import com.dxmovie.dxbase.viewmodel.BaseViewModel

class HomeModel : BaseViewModel() {


    var searchLayoutSwitch: ObservableBoolean = ObservableBoolean(true)
    var flaotSwitch: ObservableBoolean = ObservableBoolean(true)

    var floatadsSwitchCommand: BindingCommand = BindingCommand {
        flaotSwitch.set(false)
    }

    var floatadsCommand: BindingCommand = BindingCommand {
        ToastUtils.showShort("点击 float ads")
    }

    fun setSearchSwitch(switch: Boolean) {
        searchLayoutSwitch.set(switch)
    }


}