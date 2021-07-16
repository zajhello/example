package com.kingkong.common_library.utils.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class ImageTransform constructor(bitmapProcessNode: BitmapProcessNode?) : BitmapTransformation() {
    private var bitmapProcessNode: BitmapProcessNode? = null

    init {
        this.bitmapProcessNode = bitmapProcessNode
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return when {
            toTransform == null -> {
                null
            }
            bitmapProcessNode == null -> {
                toTransform
            }
            else -> {
                bitmapProcessNode!!.processBitmap(toTransform)
            }
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}