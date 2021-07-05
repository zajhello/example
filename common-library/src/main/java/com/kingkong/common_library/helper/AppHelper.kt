package com.kingkong.common_library.helper

import android.app.Activity
import android.os.SystemClock
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.dxmovie.dxbase.base.AppManager
import com.dxmovie.dxbase.utils.LoadingDialogHelper
import com.dxmovie.dxbase.utils.ToastUtils
import com.kingkong.common_library.interceptors.TokenInterceptor
import com.kingkong.common_library.router.RouterActivityPath

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/***
 * APP登出操作辅助类
 */
object AppHelper {

    /**
     *主动退出登录
     */
    const val USER_OUT = 0L

    /**
     * 多设备登录下线
     */
    const val MUTI_DEVICE = 1L

    /***强制退出***/
    const val FORCE_OUT = 2L

    const val LOGIN_RETRY = 0L
    const val LOGIN_SUCCESS = 1L
    const val LOGIN_FAILURE = 2L
    const val LOGIN_FORBIDDEN = 3L
    const val LOADING_CANCEL = 4L

    /**
     * 前后台切换时间点
     */
    private var toBackTime: Long = SystemClock.elapsedRealtime()

    private val loginFlag = AtomicBoolean(false)

    /**
     * 退出登录监听队列
     */
    private val loginOutListeners: Stack<OnLoginOutListener> = Stack()

    @JvmStatic
    fun init() {
        AppManager.getInstance().addOnAppStatusChangedListener(object : AppManager.OnAppStatusChangedListener {
            override fun onForeground(activity: Activity) {
                appStatusChanged(true)
            }

            override fun onBackground(activity: Activity) {
                appStatusChanged(false)
            }
        })
    }

    /**
     * 添加退出监听
     */
    @JvmStatic
    fun addLoginOutListener(listener: OnLoginOutListener) {
        synchronized(AppHelper::class) {
            if (loginOutListeners.contains(listener)) {
                return
            }
            loginOutListeners.push(listener)
        }
    }

    /**
     * app进入
     */
    fun appStatusChanged(foreground: Boolean) {
        //后台三个小时回来的话，强制用户登录
        if (foreground) {
            if (SystemClock.elapsedRealtime() - toBackTime > 3 * 60 * 60 * 1000) {
                loginOut()
            }
        }
        toBackTime = SystemClock.elapsedRealtime()
    }

    /**
     * 退出登录
     */
    @JvmStatic
    fun loginOut(@IntRange(from = USER_OUT, to = FORCE_OUT) type: Long = USER_OUT) {

        if (loginFlag.compareAndSet(false, true) || type == FORCE_OUT) {
            if (!AppExecutors.isMainThread()) {
                loginFlag.set(false)
                AppExecutors.runOnMainThread(Runnable { loginOut(type) })
                return
            }

            while (!loginOutListeners.isEmpty()) {
                val listener = loginOutListeners.pop()
                listener?.loginOut(type)
            }
            if (!AppManager.isAppForeground()) {
                loginFlag.set(false)
                AppManager.AppExit()
                return
            }

            resetData()

            //跳转登录
            ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
            loginFlag.set(false)
        }
    }

    @JvmStatic
    fun retryLogin(@IntRange(from = LOGIN_RETRY, to = LOADING_CANCEL) type: Long) {
        if (loginFlag.get()) {
            return
        }
        if (!AppExecutors.isMainThread()) {
            AppExecutors.runOnMainThread(Runnable { retryLogin(type) })
            return
        }
        when (type) {
            LOGIN_RETRY -> {
                ToastUtils.showShort("重新登录中")
                LoadingDialogHelper.Companion.instance.showLoading(AppHelper.javaClass.simpleName, AppManager.currentActivity())
            }
            LOGIN_SUCCESS -> {
                ToastUtils.showShort("登录成功")
                LoadingDialogHelper.Companion.instance.hideLoading(AppHelper.javaClass.simpleName)
            }
            LOGIN_FAILURE -> {
                ToastUtils.showShort("登录失败")
                LoadingDialogHelper.Companion.instance.hideLoading(AppHelper.javaClass.simpleName)
            }
            LOGIN_FORBIDDEN -> {
                ToastUtils.showShort("您所在的地区禁止登录")
                LoadingDialogHelper.Companion.instance.hideLoading(AppHelper.javaClass.simpleName)
            }
            LOADING_CANCEL -> {
                LoadingDialogHelper.Companion.instance.hideLoading(AppHelper.javaClass.simpleName)
            }
        }
    }

    /**
     * 1、注销、退出登录、被T情况下，需要把static、以及内存中的数据清空
     * 2、通过router去调用，目前认为HomeActivity被销毁的时候，默认就是上面3中情况，需要清除数据
     * 3、使用方法：
     */
    @JvmStatic
    fun resetData() {
        TokenInterceptor.setLoginAtomic(true)
    }

    interface OnLoginOutListener : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            if (owner !is OnLoginOutListener) {
                return
            }
            addLoginOutListener(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            if (owner !is OnLoginOutListener) {
                return
            }
            synchronized(AppHelper::class) {
                loginOutListeners.remove(owner)
            }
        }

        /**
         * @param type 退出登录类型
         */
        fun loginOut(@IntRange(from = USER_OUT, to = FORCE_OUT) type: Long)
    }
}