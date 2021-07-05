package com.dxmovie.dxbase.net

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import kotlin.jvm.Throws

/**
 * 网络配置接口，通过实现此接口以完成自定义网络配置
 */
interface IHttpProvider {

    /**
     * 连接超时设置
     */
    var connectTimeOut: Long

    /**
     * 读数据超时
     */
    var readTimeOut: Long

    /**
     * 写数据超时
     */
    var writeTimeOut: Long

    /**
     * 是否开启日志
     */
    fun enableLog(): Boolean = true

    /**
     * 拦截器
     */
    @Throws(Exception::class)
    fun interceptors(): Array<Interceptor> = emptyArray()

    fun networkInterceptors():Array<Interceptor> = emptyArray()

    /**
     * 网络请求自定义操作
     */
    fun requestOperator(): RequestHandle? = null

    /**
     * adapter factory配置
     */
    fun callAdapterFactory(): Array<CallAdapter.Factory> = emptyArray()

    /**
     * 数据转换factory
     */
    fun converterFactory(): Converter.Factory? = null

    /**
     * okhttpclient builder配置,对外开放扩展
     */
    fun httpBuilder(builder:OkHttpClient.Builder) {}

    fun dispatcher(): Dispatcher?

    fun connectionPool(): ConnectionPool?

}