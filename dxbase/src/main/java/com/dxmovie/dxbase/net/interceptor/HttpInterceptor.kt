package com.dxmovie.dxbase.net.interceptor

import com.dxmovie.dxbase.net.RequestHandle
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * 一个默认的http请求拦截,在请求前和请求后插入自定义操作
 */
class HttpInterceptor(private val handle: RequestHandle?): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (handle != null) {
            request = handle.onBeforeRequest(request, chain)
        }

        val response = chain.proceed(request)
        if (handle != null) {
            return handle.onAfterRequest(response, chain)
        }

        return response
    }
}