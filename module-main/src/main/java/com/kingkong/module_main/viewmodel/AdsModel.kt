package com.kingkong.module_main.viewmodel

import android.os.Message
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.dxmovie.dxbase.viewmodel.BaseViewModel
import com.kingkong.common_library.utils.UiTimer
import com.kingkong.common_library.utils.UiTimer.*

class AdsModel : BaseViewModel() {

    var statusLiveData: MutableLiveData<Boolean>? = MutableLiveData()

    private val uiTimer = UiTimer(1000)
    var timerText = ObservableField<String>("5s 跳过")
    var timeSLVisible = ObservableBoolean()


    override fun onCreate() {
        super.onCreate()

        uiTimer.setUiTimerExecuteBody(UiTimerExecuteBody { timer, timerDriver ->
            if (timerDriver is NumberTimerDriver) {
                timerText.set(String.format("%s 跳过", (timerDriver as NumberTimerDriver).currentNumber))
            } else {
                timerText.set("跳过")
            }
        }).setOnTimerStartListener(OnTimerStartListener { uiTimer, timerDriver -> timeSLVisible.set(true) })
                .setOnTimerFinishListener(OnTimerFinishListener { timer, timerDriver -> dismissAdsView() })
                .setTimerDriver(NumberTimerDriver(5, 1, 1, false, true))

        uiTimer.schedule()
    }

    private fun dismissAdsView() {
        statusLiveData?.postValue(true)
    }

    //登录按钮的点击事件
    var adOnClickCommand: BindingCommand = BindingCommand {
        dismissAdsView()
    }
}