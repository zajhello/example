package com.dxmovie.dxbase.core

/**
 * @Description APP运行环境
 */
object Environment {
    /**
     * 生产环境
     *
     *
     * 域名 正式
     * https 抓包 off
     * 日志 off
     * 混淆  on
     * signingConfig  on
     */
    const val PRODUCT = 100

    /**
     * UAT (模拟线上环境，开放一些调试工具)
     *
     *
     * 域名 正式
     * https 抓包 on
     * 日志 on
     * 混淆  on
     * signingConfig  on
     */
    const val UAT = 102
    /**
     * 开发环境
     */
    const val DEV = 104

}