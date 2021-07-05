package com.dxmovie.dxbase.net

import androidx.lifecycle.Observer

/**
 * api service变化监听类
 */
class ApiObserver<T>(val service:Class<T>, val observer: Observer<T>?) {

    fun onChanged(obj:Any) {
        observer?.onChanged(obj as T)
    }
}