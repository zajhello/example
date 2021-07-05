package com.dxmovie.dxbase.net

import android.text.TextUtils
import com.dxmovie.dxbase.core.BaseConfig
import com.dxmovie.dxbase.utils.KLog
import com.dxmovie.dxbase.utils.SPUtils
import com.dxmovie.dxbase.utils.TimeUtil
import okhttp3.HttpUrl
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


object UrlManager {


    /**
     * url key
     */
    private val KEY_URL = "urlKey"

    private val URLS = listOf(*arrayOf("http://202.60.235.20/")) //,"https://dysp.kankanfilms.com", "https://tuudf.inkdemo.cn"
    private val INDEX = AtomicInteger(0)
    private val urlLoopAvaliable = AtomicBoolean(true)

    /**
     * 当前url地址
     */
    var httpUrl: HttpUrl? = null
    var urlChanged = false

    /**
     * 获取主服务器域名
     *
     * @return
     */
    @JvmStatic
    @Synchronized
    fun getRootUrl(): String {

        if (httpUrl == null) init()

        // 正式环境不予许切换
        return if (BaseConfig.isDEV()) {
            httpUrl.toString()
        } else {
            httpUrl.toString()
        }
    }

    fun init() {
        var url = SPUtils.getInstance().getString(KEY_URL)
        if (TextUtils.isEmpty(url)) url = URLS[INDEX.get()]
        SPUtils.getInstance().put(KEY_URL, url!!)
        httpUrl = HttpUrl.parse(url)
    }

    /**
     * 更新请求域名
     */
    @Synchronized
    private fun notifyUrlChanged(url: String?) {

        if (url.isNullOrEmpty()) return
        if (httpUrl == null) return
        if (httpUrl.toString() == url) return

        var nUrl = HttpUrl.parse(url);
        STHttp.switchUrl(httpUrl.toString(), nUrl.toString())
        httpUrl = nUrl
        urlChanged = true
    }

    /**
     * 修改全局域名
     *
     * @return
     */
    @JvmStatic
    @Synchronized
    fun switchRootUrl() {

        // 控制开关
        if (!urlLoopAvaliable.get()) return

//        // 轮训时间限制 10m一次
//        if (!TimeUtil.canRetry()) return
        var index = INDEX.incrementAndGet();
        if (index >= URLS.size) {
            index = 0
        }
        INDEX.set(index)
        var rootUrl = URLS[index]
        SPUtils.getInstance().put(KEY_URL, rootUrl)
        // 刷新URL
        notifyUrlChanged(rootUrl)
    }
}