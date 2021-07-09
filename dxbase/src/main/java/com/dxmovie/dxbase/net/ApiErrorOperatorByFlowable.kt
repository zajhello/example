package com.dxmovie.dxbase.net

import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.utils.GsonUtils
import io.reactivex.FlowableOperator
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import kotlin.jvm.Throws

class ApiErrorOperatorByFlowable<T : BaseResponse<*>> constructor() : FlowableOperator<T, T> {

    @Throws(Exception::class)
    override fun apply(subscriber: Subscriber<in T>): Subscriber<in T> {
        return object : Subscriber<T> {
            override fun onSubscribe(s: Subscription) {
                subscriber.onSubscribe(s)
            }

            override fun onNext(response: T) {
                if (response.code != 0) {
                    subscriber.onError(ServiceException(GsonUtils.toJson(response), response.code, response.msg))
                    return
                }
                subscriber.onNext(response)
            }

            override fun onError(t: Throwable) {
                subscriber.onError(ExceptionHandle.handleException(t))
            }

            override fun onComplete() {
                subscriber.onComplete()
            }
        }
    }
}