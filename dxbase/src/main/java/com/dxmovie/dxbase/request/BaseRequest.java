package com.dxmovie.dxbase.request;

import java.io.Serializable;

public class BaseRequest<T> implements Serializable {


    public BaseRequest(T param){
        this.param = param;
    }

    private Long userID = 0l;
    private String token;
    private T param;
    private Integer deviceType = 2;
    private String appVersion = "daixiongshipin.sharesplit.com@1.5.8";
    private String appNameCN = "袋熊视频 测试 1.5.8";
    private String secret;
    private String devicenId = "02:15:B2:00:00:00";
    private String wechatId = "wx62d9790635beb080";
    private String taskVersion;  // 添加版本参数

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppNameCN() {
        return appNameCN;
    }

    public void setAppNameCN(String appNameCN) {
        this.appNameCN = appNameCN;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDevicenId() {
        return devicenId;
    }

    public void setDevicenId(String devicenId) {
        this.devicenId = devicenId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getTaskVersion() {
        return taskVersion;
    }

    public void setTaskVersion(String taskVersion) {
        this.taskVersion = taskVersion;
    }
}
