package com.kingkong.common_library.http.encryption

import android.text.TextUtils
import com.dxmovie.dxbase.utils.KLog
import com.facebook.stetho.json.annotation.JsonValue
import com.kingkong.common_library.utils.StringUtil
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SymmetricEncoder {

    //默认的加解密秘钥
    @JvmField
    var iv = "3236567824345678"
    @JvmField
    var DEFAULT_DECRYPT_KEY = "3136567834344678"
    @JvmField
    var DECRYPT_KEY: String? = null

    @JvmStatic
    fun getDecrypted4AES(response: String, decryptKey: String?): String? {
        if (TextUtils.isEmpty(response)) {
            return ""
        }
        KLog.i("服务端返回加密数据$response")
        val result = decrypt4AES(response, decryptKey)
        KLog.i(String.format("秘钥是 %s，解密数据 === %s", if (TextUtils.isEmpty(decryptKey)) "null" else decryptKey, result))
        return result
    }

    /**
     * MD5加密
     *
     * @param source
     * @param key
     * @return 返回加密好的串
     */
    @JvmStatic
    fun encrypt4AES(source: String?, key: String?): String? {
        return if (source == null || key == null) "" else try {
            val bytes = key.toByteArray()
            val zeroIv = IvParameterSpec(bytes)
            val key1 = SecretKeySpec(bytes, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv)
            val encryptedData = cipher.doFinal(source.toByteArray())
            StringUtil.encodeBase64(encryptedData) // 加密
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @JvmStatic
    fun encrypt4AES(source: String): String? {
        return try {
            val zeroIv = IvParameterSpec(iv.toByteArray())
            val key1 = SecretKeySpec(iv.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv)
            val encryptedData = cipher.doFinal(source.toByteArray())
            StringUtil.encodeBase64(encryptedData) // 加密
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @JvmStatic
    fun decrypt4AESbyDefaultkey(content: String): String? {
        return decrypt4AES(content, DEFAULT_DECRYPT_KEY)
    }

    @JvmStatic
    fun decrypt4AES(content: String): String? {
        return decrypt4AES(content, DECRYPT_KEY)
    }

    @JvmStatic
    fun decrypt4AES(content: String, decryptKey: String?): String {
        var decryptKey = decryptKey
        if (TextUtils.isEmpty(decryptKey)) decryptKey = ""
        return try {
            val decryptFrom = StringUtil.decodeBase64(content)
            val zeroIv = IvParameterSpec(decryptKey!!.toByteArray())
            val key1 = SecretKeySpec(decryptKey.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key1, zeroIv)
            val decryptedData = cipher.doFinal(decryptFrom)
            String(decryptedData) // 解密
        } catch (e: Exception) {
            e.printStackTrace()
            content
        }
    }



}