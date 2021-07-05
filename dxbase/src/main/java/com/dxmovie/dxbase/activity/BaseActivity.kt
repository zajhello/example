package com.dxmovie.dxbase.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dxmovie.dxbase.utils.LoadingDialogHelper
import com.dxmovie.dxbase.utils.StatusBarUtil
import com.dxmovie.dxbase.utils.extention.no
import com.dxmovie.dxbase.utils.extention.yes
import com.dxmovie.dxbase.widget.LoadingDialog
import org.greenrobot.eventbus.EventBus

/**
一些最基本的逻辑，所有的activity的基类
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        isEventbusRegistered().yes {
            EventBus.getDefault().isRegistered(this).no {
                EventBus.getDefault().register(this)
            }
        }
    }

    /**
     * 我们现在的UI设计绝大多数都是状态栏透明，然后View窗口沉浸式，所以这里简单处理
     *
     */
    open fun setStatusBar() {
        // 这里设置状态栏的背景颜色
        StatusBarUtil.changeColor(this, Color.BLACK)
    }

    /**
     * 注册EventBus
     */
    open fun isEventbusRegistered(): Boolean {
        return false
    }

    /**
     * 显示dialog
     */
    open fun showLoadingDialog(): LoadingDialog? {
        return LoadingDialogHelper.instance.showLoading(this)
    }

    /**
     * 隐藏dialog
     *
     */
    open fun hideLoadingDialog() {
        LoadingDialogHelper.instance.hideLoading(this)
    }



    override fun onDestroy() {
        super.onDestroy()
        LoadingDialogHelper.instance.clearLoading()
        isEventbusRegistered().yes {
            EventBus.getDefault().unregister(this)
        }
    }
}