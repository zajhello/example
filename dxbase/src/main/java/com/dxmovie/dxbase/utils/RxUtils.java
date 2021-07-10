package com.dxmovie.dxbase.utils;


import androidx.lifecycle.LifecycleOwner;

import com.dxmovie.dxbase.net.ApiErrorOperator;
import com.dxmovie.dxbase.net.ApiErrorOperatorByFlowable;
import com.dxmovie.dxbase.net.RetryWithDelay;
import com.dxmovie.dxbase.net.RetryWithDelayByFlowable;
import com.dxmovie.dxbase.response.BaseResponse;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 有关Rx的工具类
 */
public class RxUtils {
    /**
     * /**
     * 生命周期绑定
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleProvider lifecycle) {
        return lifecycle.bindToLifecycle();
    }

    /**
     * 线程调度器
     */
    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(2,1))
                        .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异常处理 + 线程调度器 (Infinite 的接口)
     */
    public static <T extends BaseResponse> ObservableTransformer<T, T> exceptionIoTransformer() {
        return upstream ->
                upstream.lift(new ApiErrorOperator<>())
                        .subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(2,1))
                        .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异常处理 + 线程调度器 (Infinite 的接口)
     */
    public static <T extends BaseResponse> FlowableTransformer<T, T> exceptionIoTransformerByFlowable() {
        return upstream ->
                upstream.lift(new ApiErrorOperatorByFlowable<>())
                        .subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelayByFlowable(3,1))
                        .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }
}
