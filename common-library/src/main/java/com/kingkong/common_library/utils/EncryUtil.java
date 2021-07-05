package com.kingkong.common_library.utils;

import android.text.TextUtils;

import com.dxmovie.dxbase.utils.SPUtils;
import com.kingkong.common_library.constants.Constants;
import com.kingkong.common_library.http.encryption.SymmetricEncoder;
import com.kingkong.common_library.source.Api;

import java.lang.annotation.Annotation;

import retrofit2.http.POST;

public class EncryUtil {

    public static String encrypt(String content) {
        return SymmetricEncoder.encrypt4AES(content);
    }

    public static String decrypt(String content) {
        return SymmetricEncoder.decrypt4AES(content);
    }

    public static String decryptBody(boolean isSecretRequest, String encstr) {
        String body;
        if (isSecretRequest) {
            body = SymmetricEncoder.decrypt4AESbyDefaultkey(encstr);
        } else {
            body = SymmetricEncoder.decrypt4AES(encstr);
        }
        return body;
    }

    /**
     * 是否是请求密钥接口
     *
     * @param methodAnnotations
     * @return
     */
    public static boolean isSecretRequest(Annotation[] methodAnnotations) {
        return TextUtils.equals(getRequestUrl(methodAnnotations), Api.searchResultSecret);
    }


    /**
     * 是否是请求密钥接口
     *
     * @param url
     * @return
     */
    public static boolean isSecretRequest(String url) {

        if (TextUtils.isEmpty(url)) return false;
        return TextUtils.indexOf(url, Api.searchResultSecret) != -1;
    }


    /**
     * 请求体是否加密
     *
     * @param methodAnnotations
     * @return
     */
    public static boolean isNeed2Encry(Annotation[] methodAnnotations) {
        return Api.need2Encry(getRequestUrl(methodAnnotations));
    }

    /**
     * 获取请求URL
     *
     * @param methodAnnotations
     * @return
     */
    public static String getRequestUrl(Annotation[] methodAnnotations) {

        Annotation source = methodAnnotations[0];
        POST post = (POST) source;
        String requestUrl = post.value();
        return requestUrl;
    }


    /**
     * 同步存储解密key
     *
     * @param key
     */
    public static void syncDecryptKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            SPUtils.getInstance().put(Constants.SPKEY.KEY_ENCRYPT, key);
            SymmetricEncoder.DECRYPT_KEY = key;
        }
    }

    /**
     * 同步解密key
     */
    public static void syncReadDecryptKey() {
        String key = SPUtils.getInstance().getString(Constants.SPKEY.KEY_ENCRYPT);
        if (!TextUtils.isEmpty(key))
            SymmetricEncoder.DECRYPT_KEY = key;
    }

    /**
     *
     */
    public static void init() {
        syncReadDecryptKey();
    }


}
