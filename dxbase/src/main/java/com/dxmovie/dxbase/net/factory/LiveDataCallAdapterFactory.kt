package com.dxmovie.dxbase.net.factory

import androidx.lifecycle.LiveData
import com.dxmovie.dxbase.response.BaseResponse
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 支持live data网络数据请求及返回的factory
 */
class LiveDataCallAdapterFactory: Factory() {
    override fun get(returnType: Type,
                     annotations:
                     Array<Annotation>,
                     retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != BaseResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        return LiveDataCallAdapter<BaseResponse<Any>, Any>(observableType)
    }
}