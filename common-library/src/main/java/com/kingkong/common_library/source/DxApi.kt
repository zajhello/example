package com.kingkong.common_library.source

import com.dxmovie.dxbase.request.BaseRequest
import com.dxmovie.dxbase.response.BaseResponse
import com.kingkong.common_library.request.SecretKeyRequest
import com.kingkong.common_library.response.SecretKeyModel
import com.kingkong.common_library.response.SecretKeyResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DxApi {

    /**
     * 获取平台解密key
     *
     * @return
     */
    @POST(Api.searchResultSecret)
    fun getSecretKey(@Body model: BaseRequest<SecretKeyRequest>): Observable<SecretKeyResponse>

    /**
     * 获取平台解密key
     *
     * @return
     */
    @POST(Api.searchResultSecret)
    fun syncSecretKey(@Body model: BaseRequest<SecretKeyRequest>): Call<SecretKeyResponse>





}