package com.dxmovie.dxbase.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * 网络请求执行插入操作类
 */
interface RequestHandle {
    fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request

    @Throws(IOException::class)
    fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response

}