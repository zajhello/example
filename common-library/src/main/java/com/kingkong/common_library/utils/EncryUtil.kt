package com.kingkong.common_library.utils

import android.text.TextUtils
import com.dxmovie.dxbase.utils.SPUtils
import com.kingkong.common_library.constants.Constants
import com.kingkong.common_library.http.encryption.SymmetricEncoder
import com.kingkong.common_library.source.Api
import retrofit2.http.POST

object EncryUtil {
    fun encrypt(content: String?): String? {
        return SymmetricEncoder.encrypt4AES(content!!)
    }

    fun decrypt(content: String?): String? {
        return SymmetricEncoder.decrypt4AES(content!!)
    }

    fun decryptBody(isSecretRequest: Boolean, encstr: String?): String? {
        val body: String?
        body = if (isSecretRequest) {
            SymmetricEncoder.decrypt4AESbyDefaultkey(encstr!!)
        } else {
            SymmetricEncoder.decrypt4AES(encstr!!)
        }
        return body
    }

    /**
     * 是否是请求密钥接口
     *
     * @param methodAnnotations
     * @return
     */
    @JvmStatic
    fun isSecretRequest(methodAnnotations: Array<Annotation>): Boolean {
        return TextUtils.equals(getRequestUrl(methodAnnotations), Api.searchResultSecret)
    }


    /**
     * 是否是请求密钥接口
     *
     * @param url
     * @return
     */
    @JvmStatic
    fun isSecretRequest(url: String?): Boolean {
        return if (TextUtils.isEmpty(url)) false else TextUtils.indexOf(url, Api.searchResultSecret) != -1
    }


    /**
     * 请求体是否加密
     *
     * @param methodAnnotations
     * @return
     */
    @JvmStatic
    fun isNeed2Encry(methodAnnotations: Array<Annotation>): Boolean {
        return Api.need2Encry(getRequestUrl(methodAnnotations)!!)
    }

    /**
     * 获取请求URL
     *
     * @param methodAnnotations
     * @return
     */
    @JvmStatic
    fun getRequestUrl(methodAnnotations: Array<Annotation>): String? {
        val source = methodAnnotations[0]
        val post = source as POST
        return post.value
    }


    /**
     * 同步存储解密key
     *
     * @param key
     */
    @JvmStatic
    fun syncDecryptKey(key: String?) {
        if (!TextUtils.isEmpty(key)) {
            SPUtils.getInstance().put(Constants.SPKEY.KEY_ENCRYPT, key!!)
            SymmetricEncoder.DECRYPT_KEY = key
        }
    }

    /**
     * 同步解密key
     */
    @JvmStatic
    fun syncReadDecryptKey() {
        val key = SPUtils.getInstance().getString(Constants.SPKEY.KEY_ENCRYPT)
        if (!TextUtils.isEmpty(key)) SymmetricEncoder.DECRYPT_KEY = key
    }

    /**
     *
     */
    @JvmStatic
    fun init() {
        syncReadDecryptKey()
    }

}