package com.kingkong.common_library.utils;

import androidx.databinding.ObservableInt;

import com.dxmovie.dxbase.net.ServiceException;
import com.dxmovie.dxbase.utils.NetworkUtils;
import com.dxmovie.dxbase.utils.ToastUtils;
import com.dxmovie.dxbase.widget.StatusViewLayout;

public class StatusViewUtils {

    public static void showError(ObservableInt statusObservable, Throwable throwable) {
        if (throwable instanceof ServiceException) {
            ToastUtils.showShort(throwable.getMessage());
            int code = ((ServiceException) throwable).getCode();
            // 无网络统一处理 返回 -100
            if (!NetworkUtils.isConnected()) {
                code = -100;
            }
            if (code == -100) {
                statusObservable.set(StatusViewLayout.STATUS_NONETWORK);
            } else {
                statusObservable.set(StatusViewLayout.STATUS_ERROR);
            }
        }
    }
}
