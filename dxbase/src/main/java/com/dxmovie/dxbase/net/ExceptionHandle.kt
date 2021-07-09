package com.dxmovie.dxbase.net

import android.net.ParseException
import com.dxmovie.dxbase.net.UrlManager.switchRootUrl
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import io.reactivex.exceptions.CompositeException
import io.rx_cache2.RxCacheException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ExceptionHandle {

    private const val READ_TIMEOUT = 100
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val NOT_ALLOW = 405
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val SERVICE_UNAVAILABLE = 503
    fun handleException(e: Throwable?): ServiceException {
        var e = e
        e?.printStackTrace()
        if (e is CompositeException) {
            val compositeException = e
            if (compositeException.exceptions.size > 1) {
                e = handleException(compositeException.exceptions[0])
            }
        }
        if (e is ServiceException) { //如果是该类型直接返回
            return e
        }
        val ex: ServiceException
        if (e is HttpException) {
            /**
             * HttpException 触发域名轮训
             */
            switchRootUrl()
            val httpException = e
            var message: String? = null
            message = when (httpException.code()) {
                UNAUTHORIZED -> "操作未授权"
                FORBIDDEN -> "请求被拒绝"
                NOT_FOUND, SERVICE_UNAVAILABLE -> "服务器不可用"
                REQUEST_TIMEOUT -> "服务器执行超时"
                INTERNAL_SERVER_ERROR -> "服务器内部错误"
                NOT_ALLOW -> "HTTP 405 not allowed"
                READ_TIMEOUT -> "读取数据超时"
                else -> "网络错误"
            }
            ex = ServiceException(e, httpException.code(), message)
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
                || e is MalformedJsonException) {
            ex = ServiceException(e, ERROR.PARSE_ERROR, "解析错误")
        } else if (e is ConnectException) {
            ex = ServiceException(e, ERROR.NETWORD_ERROR, "连接失败")
        } else if (e is SSLException) {
            ex = ServiceException(e, ERROR.SSL_ERROR, "证书验证失败")
        } else if (e is ConnectTimeoutException
                || e is RxCacheException) {
            ex = ServiceException(e, ERROR.TIMEOUT_ERROR, "连接超时")
        } else if (e is SocketTimeoutException) {
            ex = ServiceException(e, ERROR.TIMEOUT_ERROR, "连接超时")
        } else if (e is UnknownHostException) {
            ex = ServiceException(e, ERROR.TIMEOUT_ERROR, "网络异常、请检查网络！")
        } else if (e is CustomException) {
            ex = ServiceException(e, e.code, e.errorMessage)
        } else {
            ex = ServiceException(e, ERROR.UNKNOWN, "未知错误")
        }
        return ex
    }

    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003
        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005
        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }

}