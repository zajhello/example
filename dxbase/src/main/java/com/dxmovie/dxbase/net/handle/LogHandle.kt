package com.dxmovie.dxbase.net.handle

import com.dxmovie.dxbase.utils.GsonUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * 网络请求信息日志
 */
class LogHandle : Interceptor {
    companion object {
        /**
         * 请求列表
         * key:地址 value:次数
         */
        private val requests = ConcurrentHashMap<String, Int>()
        /**
         * 请求总数
         */
        private var requestTotal = AtomicInteger(0)

        private val startTime = System.currentTimeMillis()

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(onBeforeRequest(request))
    }

    private fun onBeforeRequest(request: Request): Request {
        val url = request.url().toString()
        var count = requests[url]?:0
        count++
        requests[url] = count
        requestTotal.incrementAndGet()
        printLog()
        return request
    }

    private fun printLog() {
        synchronized(LogHandle::class) {
            //请求五十次打印一次
            if (requestTotal.get() % 50 == 0) {
                Timber.tag("NetTotal")
                Timber.d("%s秒内，APP做了以下网络请求", (System.currentTimeMillis() - startTime) / 1000)
                Timber.tag("NetTotal")
                Timber.d(GsonUtils.toJson(requests))
            }
        }
    }


}