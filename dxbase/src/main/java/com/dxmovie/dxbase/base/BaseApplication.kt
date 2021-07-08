package com.dxmovie.dxbase.base

import android.app.Application
import android.text.TextUtils
import com.dxmovie.dxbase.core.BaseConfig.isDEV
import com.dxmovie.dxbase.utils.AppUtils
import com.dxmovie.dxbase.utils.KLog
import com.dxmovie.dxbase.utils.Utils.init

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init(this)
        //对进程做判断，推送或者有其他的进程，也会重新走onCreate
        if (TextUtils.equals(AppUtils.getCurProcessName(this), AppUtils.getPackageName(this))) { //AppManager工具初始化
            AppManager.init(this)
            //Log初始化
            KLog.init(isDEV())
            init()
        }
    }

    /**
     * 所有的初始化都放在这里执行
     */
    abstract fun init()
}