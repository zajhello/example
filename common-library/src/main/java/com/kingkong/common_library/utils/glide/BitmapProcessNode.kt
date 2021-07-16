package com.kingkong.common_library.utils.glide

import android.graphics.Bitmap

abstract class BitmapProcessNode {

    private var nextNode: BitmapProcessNode? = null

    constructor()

    open fun connect(nextNode: BitmapProcessNode?): BitmapProcessNode? {
        this.nextNode = nextNode
        return nextNode
    }

    fun processBitmap(bitmap: Bitmap?): Bitmap? {
        var bitmap = bitmap
        if (bitmap != null) {
            bitmap = onProcessBitmap(bitmap)
        }
        return nextNode?.processBitmap(bitmap) ?: bitmap
    }

    protected abstract fun onProcessBitmap(bitmap: Bitmap?): Bitmap
}