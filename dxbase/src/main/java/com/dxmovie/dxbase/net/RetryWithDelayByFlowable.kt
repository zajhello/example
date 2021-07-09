package com.dxmovie.dxbase.net

import com.dxmovie.dxbase.utils.KLog
import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class RetryWithDelayByFlowable constructor(maxRetries: Int, retryDelaySecond: Int) : Function<Flowable<Throwable>, Publisher<*>> {

    val TAG = this.javaClass.simpleName
    private var maxRetries = 0
    private var retryDelaySecond = 0
    private var retryCount = 0

    init {
        this.maxRetries = maxRetries
        this.retryDelaySecond = retryDelaySecond
    }

    @Throws(Exception::class)
    override fun apply(throwableFlowable: Flowable<Throwable>): Publisher<*> {
        return throwableFlowable.flatMap(object : Function<Throwable, Publisher<*>> {
            @Throws(Exception::class)
            override fun apply(throwable: Throwable): Publisher<*> {
                if (++retryCount <= maxRetries) { // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                    KLog.d(TAG, "Observable get error, it will try after " + retryDelaySecond
                            + " second, retry count " + retryCount)
                    return Flowable.timer(retryDelaySecond.toLong(),
                            TimeUnit.SECONDS)
                }
                // Max retries hit. Just pass the error along.
                return Flowable.error<Any>(throwable)
            }
        })
    }
}