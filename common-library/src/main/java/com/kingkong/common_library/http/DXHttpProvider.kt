package com.kingkong.common_library.http

import com.dxmovie.dxbase.core.BaseConfig
import com.dxmovie.dxbase.net.RequestHandle
import com.dxmovie.dxbase.net.STHttp
import com.dxmovie.dxbase.net.UrlManager
import com.dxmovie.dxbase.net.provider.DefaultHttpProvider
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kingkong.common_library.http.handle.DynamicUrlHandle
import com.kingkong.common_library.interceptors.DxTokenInterceptor
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.Util
import retrofit2.Converter
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class DXHttpProvider : DefaultHttpProvider() {

    private val dynamicUrlHandle by lazy { DynamicUrlHandle() }

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val instance by lazy {
            DXHttpProvider().apply {
                var url =   UrlManager.getRootUrl()
                STHttp.setBaseUrl(url)
                STHttp.setDefaultProvider(this)
            }
        }

        @JvmStatic
        fun init() = instance
    }

    /**
     * 请求前置 域名处理
     */
    override fun requestOperator(): RequestHandle? {
        return dynamicUrlHandle
    }

    /**
     * header，token拦截器
     */
    override fun interceptors(): Array<Interceptor> {
        return arrayOf(DxTokenInterceptor())
    }

    /**
     * 创建独立连接池
     */
    override fun connectionPool(): ConnectionPool? {
        return ConnectionPool(5, 10, TimeUnit.SECONDS)
    }

    /**
     * 创建独立线程池，重命名线程池以及设置okhttp最大请求数
     */
    override fun dispatcher(): Dispatcher? {
        val executorService = ThreadPoolExecutor(0,10 , 60, TimeUnit.SECONDS,
                SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false))
        val dispatcher = Dispatcher(executorService)
        dispatcher.maxRequests = 10
        return dispatcher
    }

    /**
     * facebook抓包
     */
    override fun httpBuilder(builder: OkHttpClient.Builder) {
        if (BaseConfig.isDEV()) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }
    }

    /**
     * 加解密数据转化
     */
    override fun converterFactory(): Converter.Factory? {
        return EncryConverterFactory.create()
    }
}