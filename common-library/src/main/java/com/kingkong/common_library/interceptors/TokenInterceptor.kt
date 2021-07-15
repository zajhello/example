package com.kingkong.common_library.interceptors

import android.annotation.SuppressLint
import android.text.TextUtils
import com.dxmovie.dxbase.net.ServiceException
import com.dxmovie.dxbase.utils.GsonUtils
import com.dxmovie.dxbase.utils.KLog
import com.kingkong.common_library.helper.AppHelper
import okhttp3.*
import okhttp3.internal.http.HttpCodec
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

abstract class TokenInterceptor : Interceptor {

//    val TAG = TokenInterceptor::class.java.name
//
//    // 可重入锁
//    val LOCK: Lock = ReentrantLock()
//
//    /**
//     * 是否已经跳转到登录界面 true 跳转到登录界面 false 还没有跳转
//     */
//    private val toLogin = AtomicBoolean(true)

    @SuppressLint("CheckResult")
    override fun intercept(chain: Interceptor.Chain): Response? {
        var request = chain.request()
        return chain.proceed(request)

//        return try {
//            val response = chain.proceed(request)
//
//            /** 解密key过期操作 */
//            val decryBody = isDeckeyExpired(response)
//            if (TextUtils.isEmpty(decryBody)) {
//                var deckey: String? = null
//                if (LOCK.tryLock()) {
//                    deckey = try {
//                        refreshDeckeySync()
//                    } finally {
//                        LOCK.unlock()
//                    }
//                }
//                Timber.tag(TAG).d("deckey：%s", deckey)
//                request = buildNewRequest(request)
//                return chain.proceed(request)
//            }
//            /** token过期操作 */
//            if (isTokenExpired(decryBody) && !toLogin.get()) {
//                KLog.e("isTokenExpired", "   =====" + toLogin.get())
//                if (LOCK.tryLock()) {
//                    try {
//                        toLoginActivity(AppHelper.USER_OUT)
//                    } finally {
//                        LOCK.unlock()
//                    }
//                }
//            }
//
//            val body = ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), decryBody)
//            response.newBuilder()
//                    .body(body)
//                    .build()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            var errorMessage = e.localizedMessage
//            if (TextUtils.isEmpty(errorMessage)) errorMessage = e.toString()
//            val errorResponse = ServiceException(e, -1, errorMessage)
//            val responseBody = ResponseBody.create(MediaType.parse("json"), GsonUtils.toJson(errorResponse))
//            Response.Builder().request(request)
//                    .body(responseBody)
//                    .code(HttpCodec.DISCARD_STREAM_TIMEOUT_MILLIS) // 这里需要跟踪
//                    .message(e.message)
//                    .protocol(Protocol.HTTP_2)
//                    .build()
//        }
    }

//    /**
//     * 判断解密key是否过期
//     */
//    abstract fun isDeckeyExpired(response: Response?): String?
//
//    /**
//     * 同步刷新key
//     */
//    abstract fun refreshDeckeySync(): String?
//
//    /**
//     * 判断token是否过期
//     */
//    abstract fun isTokenExpired(decryBody: String?): Boolean
//
//    /**
//     * 构建新的请求
//     *
//     * @param oldRequest
//     * @return
//     */
//    abstract fun buildNewRequest(oldRequest: Request?): Request?
//
//    open fun setLoginAtomic(islogin: Boolean) {
//        toLogin.set(islogin)
//    }
//
//    open fun toLoginActivity(status: Long) {
//        AppHelper.loginOut(status)
//    }
}