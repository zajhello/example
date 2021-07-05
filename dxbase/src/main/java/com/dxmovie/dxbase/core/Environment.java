package com.dxmovie.dxbase.core;

/**
 * @Description APP运行环境
 */
public class Environment {
    /**
     * 生产环境
     * <p>
     * 域名 正式
     * https 抓包 off
     * 日志 off
     * 混淆  on
     * signingConfig  on
     */
    public static final int PRODUCT = 100;

    /**
     * UAT (模拟线上环境，开放一些调试工具)
     * <p>
     * 域名 正式
     * https 抓包 on
     * 日志 on
     * 混淆  on
     * signingConfig  on
     */
    public static final int UAT = 102;
    /**
     * 开发环境
     */
    public static final int DEV = 104;

}
