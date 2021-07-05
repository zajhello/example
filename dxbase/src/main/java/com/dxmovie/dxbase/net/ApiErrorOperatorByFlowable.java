package com.dxmovie.dxbase.net;


import com.dxmovie.dxbase.response.BaseResponse;
import com.dxmovie.dxbase.utils.GsonUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.FlowableOperator;


public final class ApiErrorOperatorByFlowable<T extends BaseResponse> implements FlowableOperator<T, T> {

    public ApiErrorOperatorByFlowable() {
    }

    @Override
    public Subscriber<? super T> apply(Subscriber<? super T> subscriber) throws Exception {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscriber.onSubscribe(s);
            }

            @Override
            public void onNext(T response) {
                if (response.getCode() != 0) {
                    subscriber.onError(new ServiceException(GsonUtils.toJson(response), response.getCode(), response.getMsg()));
                    return;
                }

                subscriber.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                subscriber.onError(ExceptionHandle.handleException(t));
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        };
    }
}