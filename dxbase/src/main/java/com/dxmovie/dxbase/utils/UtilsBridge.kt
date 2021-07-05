package com.dxmovie.dxbase.utils

import android.annotation.SuppressLint
import com.dxmovie.dxbase.utils.extention.yes

/**
 *@author Levi
 *@date 2020/5/14
 *@desc 桥梁工具 java、kotlin
 */
object UtilsBridge {

    const val BYTE = 1
    const val KB = 1024
    const val MB = 1048576
    const val GB = 1073741824

    /** 是否包含的有空格 */
    @JvmStatic
    fun isSpace(s: String): Boolean = s.isSpace()


    /**
     * Size of byte to fit size of memory.
     *
     * to three decimal places
     *
     * @param byteSize  Size of byte.
     * @param precision The precision
     * @return fit size of memory
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun byte2FitMemorySize(byteSize: Long, precision: Int = 3): String? {
        require(precision >= 0) { "precision shouldn't be less than zero!" }
        return if (byteSize < 0) {
            throw IllegalArgumentException("byteSize shouldn't be less than zero!")
        } else if (byteSize < KB) {
            String.format("%." + precision + "fB", byteSize.toDouble())
        } else if (byteSize < MB) {
            String.format("%." + precision + "fKB", byteSize.toDouble() / KB)
        } else if (byteSize < GB) {
            String.format("%." + precision + "fMB", byteSize.toDouble() / MB)
        } else {
            String.format("%." + precision + "fGB", byteSize.toDouble() / GB)
        }
    }

}

fun String.isSpace(): Boolean {
    if (isEmpty()) {
        return true
    }
    forEach {
        Character.isWhitespace(it).yes {
            return false
        }
    }
    return true
}