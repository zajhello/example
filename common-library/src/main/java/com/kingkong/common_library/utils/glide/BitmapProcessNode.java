package com.kingkong.common_library.utils.glide;

import android.graphics.Bitmap;
/**
 * 位图处理点
 */
public abstract class BitmapProcessNode {
    private BitmapProcessNode nextNode;

    public BitmapProcessNode() {}

    BitmapProcessNode connect(BitmapProcessNode nextNode) {
        this.nextNode = nextNode;
        return nextNode;
    }

    public final Bitmap processBitmap(Bitmap bitmap) {
        if(bitmap != null) {
            bitmap = onProcessBitmap(bitmap);
        }
        if(nextNode != null) {
            return nextNode.processBitmap(bitmap);
        }
        return bitmap;
    }

    protected abstract Bitmap onProcessBitmap(Bitmap bitmap);
}