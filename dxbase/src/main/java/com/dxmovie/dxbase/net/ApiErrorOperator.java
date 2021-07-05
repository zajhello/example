package com.dxmovie.dxbase.net;


import com.dxmovie.dxbase.response.BaseResponse;
import com.dxmovie.dxbase.utils.GsonUtils;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description 网络异常过滤
 */
public final class ApiErrorOperator<T extends BaseResponse> implements ObservableOperator<T, T> {

    public ApiErrorOperator() {

    }

    @Override
    public Observer<? super T> apply(final Observer<? super T> observer) {
        return new Observer<T>() {
            @Override
            public void onComplete() {
                observer.onComplete();
            }

            @Override
            public void onError(final Throwable e) {
                observer.onError(ExceptionHandle.handleException(e));
            }

            @Override
            public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override
            public void onNext(T response) {
                //
                if (response.getCode() != 0) {
                    observer.onError(new ServiceException(GsonUtils.toJson(response), response.getCode(), response.getMsg()));
                    return;
                }

                observer.onNext(response);
            }
        };
    }
}