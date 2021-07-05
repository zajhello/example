package com.kingkong.common_library.http.encryption;

import android.text.TextUtils;

import com.dxmovie.dxbase.utils.KLog;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.kingkong.common_library.utils.StringUtil.decodeBase64;
import static com.kingkong.common_library.utils.StringUtil.encodeBase64;


public class SymmetricEncoder {
    //默认的加解密秘钥
    public static String iv = "3236567824345678";
    public static String DEFAULT_DECRYPT_KEY = "3136567834344678";
    public static String DECRYPT_KEY;

    public static String getDecrypted4AES(String response, String decryptKey) {
        if (TextUtils.isEmpty(response)) {
            return "";
        }
        KLog.i("服务端返回加密数据".concat(response));
        String result = SymmetricEncoder.decrypt4AES(response, decryptKey);
        KLog.i(String.format("秘钥是 %s，解密数据 === %s", TextUtils.isEmpty(decryptKey) ? "null" : decryptKey, result));
        return result;
    }

    /**
     * MD5加密
     *
     * @param source
     * @param key
     * @return 返回加密好的串
     */
    public static String encrypt4AES(String source, String key) {
        if (source == null || key == null) return "";
        try {
            byte[] bytes = key.getBytes();
            IvParameterSpec zeroIv = new IvParameterSpec(bytes);
            SecretKeySpec key1 = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);
            byte[] encryptedData = cipher.doFinal(source.getBytes());
            return encodeBase64(encryptedData); // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encrypt4AES(String source) {
        try {

            IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
            SecretKeySpec key1 = new SecretKeySpec(iv.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);
            byte[] encryptedData = cipher.doFinal(source.getBytes());
            String encryptResultStr = encodeBase64(encryptedData);
            return encryptResultStr; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt4AESbyDefaultkey(String content) {
        return decrypt4AES(content, DEFAULT_DECRYPT_KEY);
    }

    public static String decrypt4AES(String content) {
        return decrypt4AES(content, DECRYPT_KEY);
    }

    public static String decrypt4AES(String content, String decryptKey) {

        if (TextUtils.isEmpty(decryptKey)) decryptKey = "";

        try {
            byte[] decryptFrom = decodeBase64(content);

            IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes());
            SecretKeySpec key1 = new SecretKeySpec(decryptKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key1, zeroIv);
            byte decryptedData[] = cipher.doFinal(decryptFrom);
            return new String(decryptedData); // 解密
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
    }
}
