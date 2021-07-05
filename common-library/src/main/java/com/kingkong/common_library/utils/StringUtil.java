package com.kingkong.common_library.utils;

import android.os.Bundle;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class StringUtil {

    private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
            -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
            38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
            -1, -1};


    /**
     * 补全不完整的url地址
     */
    public static String complementUrl(String url) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 判定字符串是否为空，该方法会先去除字符串连边的空格，使用处理过后的字符串进行判断
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null) {
            return true;
        } else {
            if (str instanceof String) {
                return ((String) str).trim().length() == 0;
            } else {
                return str.length() == 0;
            }
        }
    }

    /**
     * 返回字符串，如果这个字符串为 null，则返回 ""
     */
    public static String toString(String str) {
        return str == null ? "" : str;
    }


    /**
     * 编码常规字符串为base64字符串
     */
    public static String encodeBase64(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4)
                        | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4)
                    | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
                    | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * 解码base64字符串为常规字符串
     */
    public static byte[] decodeBase64(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1)
                break;
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) (((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("iso8859-1");
    }

    /**
     * 根据指定的长度对数字补够长度的部分用零补全
     * 1 ==> 0001
     */
    public static String fixZeroByCount(int number, int count) {
        String numStr = String.valueOf(number);
        int needFixLength = count - numStr.length();
        StringBuffer sb = new StringBuffer(numStr);
        for (int i = 0; i < needFixLength; i++) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    /**
     * 将一个字符串转化为为MD5字符串
     */
    public static String encodeToMD5(String str) {
        if (str == null) {
            throw new NullPointerException("str cannot be null");
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest();            // MD5 的计算结果是一个 128 位的长整数， 用字节表示就是 16 个字节
            char strs[] = new char[16 * 2];        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            int k = 0;                                // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {            // 从第一个字节开始，对 MD5 的每一个字节,转换成 16 进制字符的转换
                byte byte0 = tmp[i];                // 取第 i 个字节
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf];        // 取字节中高 4 位的数字转换,>>> 为逻辑右移，将符号位一起右移
                strs[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
            }
            return new String(strs);                // 换后的结果转换为字符串
        } catch (Exception e) {
            throw new IllegalStateException("encode to md5 failed", e);
        }
    }

    /**
     * 将字节大小的数字单位转化为易读的字符串字符串，该方法会根据字节的大小自动转化为 GB、MB、KB、B 单位的字符串
     */
    public static String longSizeToStrSize(long size) {
        long MAX_B = 1024;                // 定义单位最大值
        long MAX_KB = MAX_B * 1024;
        long MAX_MB = MAX_KB * 1024;
        long MAX_GB = MAX_MB * 1024;
        String result = null;
        if (size <= 0) {                    // 无大小
            result = "0B";
        } else if (size < MAX_B) {        // B
            result = size + "B";
        } else if (size < MAX_KB) {        // KB
            float kb = (float) ((double) size / (double) MAX_B);
            result = String.format("%.2fKB", kb);
        } else if (size < MAX_MB) {        // MB
            float mb = (float) ((double) size / (double) MAX_KB);
            result = String.format("%.2fMB", mb);
        } else if (size < MAX_GB) {        // GB
            float gb = (float) ((double) size / (double) MAX_MB);
            result = String.format("%.2fGB", gb);
        } else {
            throw new IllegalArgumentException("size " + size + " too large to display");
        }
        return result;
    }

    /**
     * 判断一个字符串是否在一个字符串数组中
     * 空字符将被判定为不在字符数组中、字符数组中的空元素不参与比较
     */
    public static boolean isInStringArray(String string, String[] stringArray) {
        if (string == null || string.length() == 0) {
            return false;
        } else if (stringArray == null || stringArray.length == 0) {
            return false;
        } else {
            for (String str : stringArray) {
                if (str != null && str.equals(string)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * 截取字符串 （支持正向、反向截取）
     * 使用正负数表示截取的方向
     *
     * @param string 待截取的字符串
     * @param length 长度
     */
    public static String subString(String string, int length) {
        if (string == null || string.length() == 0) {
            return "";
        } else {
            int start = length >= 0 ? 0 : string.length() + length;
            int end = length >= 0 ? length : string.length();
            if (start < 0) {
                start = 0;
            }
            if (end > string.length()) {
                end = string.length();
            }
            return string.substring(start, end);
        }
    }

    /**
     * 截取字符串 （支持正向、反向截取）
     * 位置从0开始算起，使用正负数表示截取的方向
     *
     * @param string 待截取的字符串
     * @param start  截取起始位置
     * @param end    截取结束位置
     */
    public static String subString(String string, int start, int end) {
        if (string == null || string.length() == 0) {
            return "";
        } else {
            // 正向 start <= end，start end 都需要大于等于0， start end 都需要在string的长度区间内，不在区间的矫正区间
            // 反向 start >= end，start end 都需要小于等于0， start end 的绝对值都需要在string的长度区间内，不在区间的矫正区间
            // 其它情况作异常处理，直接返回""字符串
            int startIndex, endIndex;
            if (start <= end && start >= 0 && end >= 0) {
                startIndex = start;
                endIndex = end + 1;
            } else if (start >= end && start <= 0 && end <= 0) {
                startIndex = string.length() + end - 1;
                endIndex = string.length() + start;
            } else {
                return "";
            }
            if (startIndex < 0) {
                startIndex = 0;
            }
            if (endIndex > string.length()) {
                endIndex = string.length();
            }
            return string.substring(startIndex, endIndex);
        }
    }

    public static String toString(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bundle {\n");
        for (String key : bundle.keySet()) {
            stringBuffer.append(key + " => " + bundle.get(key) + "\n");
        }
        stringBuffer.append("\n}");
        return stringBuffer.toString();
    }
}
