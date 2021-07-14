package com.kingkong.common_library.http.handle

import com.dxmovie.dxbase.net.RequestHandle
import com.dxmovie.dxbase.net.UrlManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

/**
 * 请求域名动态切换处理器
 */
class DynamicUrlHandle : RequestHandle {

    override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {

        if (UrlManager.httpUrl == null) {
            val url: String = request.url().toString()
            val pathStart: Int = url.indexOf('/', request.url().scheme().length + 3)
            val domain = url.substring(0, pathStart)
            UrlManager.httpUrl = HttpUrl.parse(domain)
            return request
        }

        if (!UrlManager.urlChanged) return request

        val requestUrl = request.url()
        if (requestUrl.scheme() == UrlManager.httpUrl!!.scheme()
                && requestUrl.host() == UrlManager.httpUrl!!.host()
                && requestUrl.port() == UrlManager.httpUrl!!.port()) {
            Timber.i("[域名切换]请求：%s 域名与当前配置：%s域名 相同不做切换", requestUrl.toString(), UrlManager.httpUrl.toString())
            return request
        }

        Timber.i("[域名切换]请求：%s域名与当前配置：%s域名 不同自动切换", requestUrl.toString(), UrlManager.httpUrl.toString())
        //创建新的请求域名
        val newHttpUrl = requestUrl.newBuilder()
                .scheme(UrlManager.httpUrl!!.scheme())
                .host(UrlManager.httpUrl!!.host())
                .port(UrlManager.httpUrl!!.port())
                .build()

//        KLog.e("onBeforeRequest",newHttpUrl.toString())
        return request.newBuilder().url(newHttpUrl).build()
    }

    override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
        return response
    }
}