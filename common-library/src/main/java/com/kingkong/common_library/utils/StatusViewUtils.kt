package com.kingkong.common_library.utils

import androidx.databinding.ObservableInt
import com.dxmovie.dxbase.net.ServiceException
import com.dxmovie.dxbase.utils.NetworkUtils
import com.dxmovie.dxbase.utils.ToastUtils
import com.dxmovie.dxbase.widget.StatusViewLayout

class StatusViewUtils {
    companion object{
        @JvmStatic
        fun showError(statusObservable: ObservableInt, throwable: Throwable) {
            if (throwable is ServiceException) {
                ToastUtils.showShort(throwable.message)
                var code = throwable.code
                // 无网络统一处理 返回 -100
                if (!NetworkUtils.isConnected()) {
                    code = -100
                }
                if (code == -100) {
                    statusObservable.set(StatusViewLayout.STATUS_NONETWORK)
                } else {
                    statusObservable.set(StatusViewLayout.STATUS_ERROR)
                }
            }
        }
    }
}