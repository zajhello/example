package com.dxmovie.dxbase.net;


import com.dxmovie.dxbase.utils.KLog;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class RetryWithDelayByFlowable implements Function<Flowable<Throwable>, Publisher<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public RetryWithDelayByFlowable(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }


    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
            @Override
            public Publisher<?> apply(Throwable throwable) throws Exception {
                if (++retryCount <= maxRetries) {
                    // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                    KLog.d(TAG, "Observable get error, it will try after " + retryDelaySecond
                            + " second, retry count " + retryCount);
                    return Flowable.timer(retryDelaySecond,
                            TimeUnit.SECONDS);
                }
                // Max retries hit. Just pass the error along.
                return Flowable.error(throwable);
            }
        });
    }
}