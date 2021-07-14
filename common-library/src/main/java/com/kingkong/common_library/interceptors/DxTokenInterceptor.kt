package com.kingkong.common_library.interceptors

import com.dxmovie.dxbase.net.UrlManager
import com.dxmovie.dxbase.request.BaseRequest
import com.dxmovie.dxbase.utils.GsonUtils
import com.dxmovie.dxbase.utils.KLog
import com.dxmovie.dxbase.utils.extention.yes
import com.google.gson.reflect.TypeToken
import com.kingkong.common_library.http.DxConverterFactory
import com.kingkong.common_library.request.SecretKeyRequest
import com.kingkong.common_library.response.SecretKeyResponse
import com.kingkong.common_library.source.DxApi
import com.kingkong.common_library.utils.EncryUtil
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @Description  解密检查具体实现方法
 */
class DxTokenInterceptor : TokenInterceptor() {


    override fun buildNewRequest(oldRequest: Request?): Request {
        return oldRequest!!.newBuilder()
                .build()
    }

    override fun refreshDeckeySync(): String {

        val call = syncSecretkey();
        val response = call.execute()
        var key = ""

        response.isSuccessful.yes {
            key = response.body()!!.datas!!.secret
            EncryUtil.syncDecryptKey(key)
        }
        return key
    }

    /**
     * 解密key是否过期
     */
    override fun isDeckeyExpired(response: Response?): String {

        var httpUrl = response!!.request().url().toString()
        val responseBody = response.peekBody(Long.MAX_VALUE)
        var body = responseBody.string()
        body = EncryUtil.decryptBody(EncryUtil.isSecretRequest(httpUrl), body)
        return body
    }

    override fun isTokenExpired(decryBody: String?): Boolean {
        val fromJson: SecretKeyResponse = getSecretKeyModel(decryBody)
        KLog.e("isTokenExpired", fromJson.code == 101)
        return fromJson.code == 101
    }

    private fun getSecretKeyModel(decryBody: String?): SecretKeyResponse {
        val type: Type = object : TypeToken<SecretKeyResponse>() {}.type
        return GsonUtils.fromJson(decryBody, type)
    }

    fun syncSecretkey(): Call<SecretKeyResponse> {

        val retrofit = Retrofit.Builder()
                .baseUrl(UrlManager.getRootUrl())
                .addConverterFactory(DxConverterFactory.create(true))
                .build()
        return retrofit.create(DxApi::class.java).syncSecretKey(BaseRequest(SecretKeyRequest()))
    }
}