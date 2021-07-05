@file:JvmName("RxExt")

package com.dxmovie.dxbase.net

import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.utils.GsonUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 *
 * 一个rx数据结果扩展
 * 使用方法例如：
 * 1.kotlin:
 *  Observable.just(1).call({ i ->

}, { throwable ->

})

 * 2.java:
 * RxExt.call(Observable.just(1)).subscribe(integer -> {

}, { throwable ->

);
 */

@JvmOverloads
fun <T> Observable<T>.call(success: ((T) -> Unit)? = null,
                           error: ((ServiceException) -> Unit)? = null,
                           finish: (() -> Unit)? = null,
                           start: ((Disposable) -> Unit)? = null): Disposable {
    val proxy = RxObservableProxy(this)
    proxy.subscribe(Consumer {
        success?.invoke(it)
    }, Consumer {
        error?.invoke(it)
    }, Action {
        finish?.invoke()
    },
    Consumer {
        start?.invoke(it)
    })
    return proxy
}

/**
 * 此扩展专用于java中使用
 */
fun <T> Observable<T>.call(): RxObservableProxy<T> {
    return RxObservableProxy(this)
}

/**
 * 获取错误码
 */
fun Throwable.code(): Int {
    if(this is ServiceException) {
        return this.code
    }
    return -1
}

/**
 * 获取用户提示信息
 */
fun Throwable.show(): String {
    if(this is ServiceException) {
        return this.message!!
    }
    val ex = ExceptionHandle.handleException(this)
    return ex.message!!
}

/**
 * 获取异常中的响应体
 */
fun Throwable.response(): BaseResponse<Any>? {
    if(this is ServiceException) {
        if (this.realException.startsWith("{") && this.realException.endsWith("}")) {
            return GsonUtils.fromJson<BaseResponse<Any>>(this.realException, BaseResponse::class.java)
        }
    }
    return null
}

open class RxObservableProxy<T>(private val source: Observable<T>) : MainThreadDisposable() {

    private var disposable: Disposable? = null

    private fun getServiceException(e: Throwable): ServiceException {
        var ex = e
        if (e !is ServiceException) {
            ex = ExceptionHandle.handleException(e)
        }
        return ex as ServiceException
    }

    override fun onDispose() {
        disposable?.dispose()
    }

    /**
     * 仿造Observable一系列subscribe订阅
     */
    @JvmOverloads
    open fun subscribe(
            onNext: Consumer<in T>?,
            onError: Consumer<in ServiceException>?,
            onComplete: Action?,
            onSubscribe: Consumer<in Disposable>?): Disposable? {
        disposable?.dispose()
        disposable = source.subscribe(onNext, Consumer {
            onError?.accept(getServiceException(it))
        }, onComplete, onSubscribe)
        return disposable
    }

    open fun subscribe(observer: Observer<in T>? = null): Disposable {
        if(observer == null) {
            disposable = source.subscribe()
        }else {
            source.subscribe(observer)
        }
        return this
    }
}