package com.dxmovie.dxbase.net

import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.utils.GsonUtils
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ApiErrorOperator <T : BaseResponse<*>>  constructor() : ObservableOperator<T, T> {

    override fun apply(observer: Observer<in T>): Observer<in T> {
        return object : Observer<T> {
            override fun onComplete() {
                observer.onComplete()
            }

            override fun onError(e: Throwable) {
                observer.onError(ExceptionHandle.handleException(e))
            }

            override fun onSubscribe(d: Disposable) {
                observer.onSubscribe(d)
            }

            override fun onNext(response: T) { //
                if (response.code != 0) {
                    observer.onError(ServiceException(GsonUtils.toJson(response), response.code, response.msg))
                    return
                }
                observer.onNext(response)
            }
        }
    }
}