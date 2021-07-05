package com.dxmovie.dxbase.net.provider

import com.dxmovie.dxbase.net.IHttpProvider
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import kotlin.jvm.Throws

/**
 */
open class BaseHttpProvider: IHttpProvider {

    override var connectTimeOut: Long = 24L

    override var readTimeOut: Long = 24L

    override var writeTimeOut: Long = 24L

    override fun enableLog(): Boolean {
        return super.enableLog()
    }

    @Throws(Exception::class)
    override fun interceptors(): Array<Interceptor> {
        return super.interceptors()
    }

    override fun callAdapterFactory(): Array<CallAdapter.Factory> {
        return super.callAdapterFactory()
    }

    override fun converterFactory(): Converter.Factory? {
        return super.converterFactory()
    }

    override fun httpBuilder(builder: OkHttpClient.Builder) {
        super.httpBuilder(builder)
    }

    override fun dispatcher(): Dispatcher? = null

    override fun connectionPool(): ConnectionPool? = null
}