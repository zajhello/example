package com.dxmovie.dxbase.response


import com.dxmovie.dxbase.net.ExceptionHandle
import com.dxmovie.dxbase.net.Status
import java.io.Serializable

/**
 * 实际业务返回的固定字段, 根据需求来定义，
 */
open class BaseResponse<T> constructor() : Serializable {
    var code = 0
    var msg: String? = null
    var datas: T? = null
    var status: Status? = null

    constructor(code:Int = 0,
                msg: String? = "",
                datas: T? = null,
                status: Status? = null):this() {
        this.code = code
        this.msg = msg
        this.datas = datas
        this.status = status
    }

    override fun toString(): String {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + datas +
                ", status=" + status
                '}'
    }

    companion object {
        /**
         * 解析网络返回结果
         */
        fun <T> parseResponse(body: BaseResponse<T>): BaseResponse<T> {
            if (body.code != 0) {
                return BaseResponse(body.code, body.msg, body.datas, Status.ERROR)
            }
            if (body.datas == null) {
                return BaseResponse(body.code, body.msg, body.datas, Status.EMPTY)
            }

            val data = body.datas as Any
            return BaseResponse(body.code, body.msg, data as T, Status.SUCCESS)
        }

        fun <T> create(error: Throwable): BaseResponse<T> {
            val e = ExceptionHandle.handleException(error)
            return BaseResponse(e.code, e.message, null, Status.ERROR)
        }

    }

    fun isSuccessful(): Boolean {
        return status == Status.SUCCESS
    }

    fun isEmpty(): Boolean {
        return status == null || status == Status.EMPTY
    }

    fun isError(): Boolean {
        return status == Status.ERROR
    }

    fun isLoadding(): Boolean {
        return status == Status.LOADING
    }

}