package com.dxmovie.dxbase.net;

import android.text.TextUtils;

import com.dxmovie.dxbase.utils.NetworkUtils;

/**
 * @Description 自定义异常
 */
public class ServiceException extends Exception {
    private int code;
    /**
     * 面向用户的message
     */
    private String showMessage;

    public ServiceException(Throwable throwable, int code, String showMessage) {
        super(throwable);
        this.code = code;
        this.showMessage = showMessage;
    }

    public ServiceException(String realData, int code, String showMessage) {
        super(realData);
        this.code = code;
        this.showMessage = showMessage;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(showMessage)) {
            return showMessage;
        }
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        return "未知异常";
    }

    /**
     * 返回真实异常信息
     *
     * @return
     */
    public String getRealException() {
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        if (!TextUtils.isEmpty(showMessage)) {
            return showMessage;
        }
        return "未知异常";
    }

    public void setMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
