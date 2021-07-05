package com.dxmovie.dxbase.net.factory

import androidx.lifecycle.LiveData
import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.net.ExceptionHandle
import com.dxmovie.dxbase.net.Status
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 支持将retrofit返回的结果转换成live data的结果集
 *
*/
class LiveDataCallAdapter<T: BaseResponse<R>, R>(private val responseType: Type) :
        CallAdapter<T, LiveData<BaseResponse<R>>> {

    override fun adapt(call: Call<T>): LiveData<BaseResponse<R>> {
        return object : LiveData<BaseResponse<R>>() {
            private var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<T> {
                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body == null || response.code() == 204) {
                                    postValue(BaseResponse(204, "服务器未返回内容", null, Status.EMPTY))
                                    return
                                }
                                postValue(BaseResponse.parseResponse(body))
                            } else {
                                val message = response.errorBody()?.string()
                                val error = if (message.isNullOrEmpty()) {
                                    response.message()
                                } else {
                                    message
                                }
                                postValue(BaseResponse(response.code(), error, null, Status.ERROR))
                            }
                        }

                        override fun onFailure(call: Call<T>, throwable: Throwable) {
                            val e = ExceptionHandle.handleException(throwable)
                            postValue(BaseResponse(e.code, e.message, null, Status.ERROR))
                        }
                    })
                }
            }

            override fun onInactive() {
                super.onInactive()
                if(!hasObservers()) {
                    call.cancel()
                }
            }
        }
    }

    override fun responseType(): Type = responseType
}