package com.dxmovie.dxbase.net.provider

import android.os.Build
import com.dxmovie.dxbase.base.AppManager
import com.dxmovie.dxbase.core.BaseConfig
import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.utils.AppUtils
import com.dxmovie.dxbase.utils.DeviceUtils
import com.dxmovie.dxbase.utils.GsonUtils
import com.dxmovie.dxbase.utils.MD5Util
import okhttp3.*
import okhttp3.internal.http.HttpCodec
import kotlin.jvm.Throws

/**
 * 默认的网络配置器
 * 外部可继承此类，实现默认网络配置扩展
 */
open class DefaultHttpProvider: BaseHttpProvider() {

    @Throws(Exception::class)
    override fun interceptors(): Array<Interceptor> {
        val defaultInterceptor = Interceptor { chain ->
            return@Interceptor checkAllowDefault(chain)
                    ?: try {
                        val headers = mutableMapOf<String, String>()
                        buildHeaders(headers)
                        //构建新的请求builder
                        val builder = chain.request().newBuilder()
                        headers.forEach {
                            builder.addHeader(it.key, it.value)
                        }

                        chain.proceed(builder.build())

                    } catch (e:Exception) {
                        e.printStackTrace()
                        val resp = BaseResponse<Nothing>()
                        resp.code = -1
                        resp.msg = e.message?:e.toString()
                        val responseBody = ResponseBody.create(MediaType.parse("json"), GsonUtils.toJson(resp))
                        Response.Builder().request(chain.request())
                                .body(responseBody)
                                .code(HttpCodec.DISCARD_STREAM_TIMEOUT_MILLIS)
                                .message(e.message?:e.toString())
                                .protocol(Protocol.HTTP_2)
                                .build()
                    }
        }

        val interceptors = addInterceptors()
        if (interceptors.isEmpty()) {
            return arrayOf(defaultInterceptor)
        }
        return arrayOf(defaultInterceptor).plus(interceptors)
    }

    /**
     * 开发环境下打开日志
     */
    override fun enableLog(): Boolean {
        return BaseConfig.isDEV()
    }

    /**
     * 对外开放扩展头信息修改
     */
    open fun buildHeaders(headers:MutableMap<String,String>) {}

    /**
     * 检查是否添加公共头信息
     * @return response 如果为空，添加公共头信息；不为空，使用自定义头信息
     */
    open fun checkAllowDefault(chain:Interceptor.Chain): Response? = null

    /**
     * 对外开放扩展添加拦截器
     */
    open fun addInterceptors(): Array<Interceptor> = emptyArray()

}