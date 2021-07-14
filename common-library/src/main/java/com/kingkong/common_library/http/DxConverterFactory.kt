package com.kingkong.common_library.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.kingkong.common_library.http.encryption.SymmetricEncoder.encrypt4AES
import com.kingkong.common_library.utils.EncryUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class DxConverterFactory : Converter.Factory {

    private var gson: Gson? = null
    private var decryptContent = false

    companion object {
        private val TAG = DxConverterFactory::class.java.name
        fun create(): DxConverterFactory? {
            return create(Gson(), false)
        }

        fun create(decryptContent: Boolean): DxConverterFactory? {
            return create(Gson(), decryptContent)
        }

        private fun create(gson: Gson, decryptContent: Boolean): DxConverterFactory? {
            return DxConverterFactory(gson, decryptContent)
        }
    }

    constructor(gson: Gson?, decryptContent: Boolean) {
        if (gson == null) throw NullPointerException("Gson object can not be null")
        this.gson = gson
        this.decryptContent = decryptContent
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>, retrofit: Retrofit?): Converter<ResponseBody, *> {
        return DecodeResponseBodyConverter<Any>(gson!!, type!!, annotations, decryptContent)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation?>?, methodAnnotations: Array<Annotation>, retrofit: Retrofit?): Converter<*, RequestBody> {
        return EncodeRequestBodyConverter<Any>(gson!!, type!!, methodAnnotations)
    }

    internal class DecodeResponseBodyConverter<T>(private val gson: Gson, private val type: Type, private val annotations: Array<Annotation>, private val decryptContent: Boolean) : Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(responseBody: ResponseBody): T {
            val adapter = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<T>
            var body = responseBody.string()
            if (decryptContent) body = EncryUtil.decryptBody(EncryUtil.isSecretRequest(annotations), body)
            return adapter.fromJson(body)
        }
    }

    internal class EncodeRequestBodyConverter<T>(private val gson: Gson, var type: Type, var methodAnnotations: Array<Annotation>) : Converter<T, RequestBody> {
        @Throws(IOException::class)
        override fun convert(o: T): RequestBody {
            val adapter = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<T>
            var body = adapter.toJson(o)
            if (EncryUtil.isNeed2Encry(methodAnnotations)) body = encrypt4AES(body!!)
            return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body)
        }
    }
}