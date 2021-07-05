package com.kingkong.common_library.utils.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 图片转换
 */
public final class ImageTransform extends BitmapTransformation {
    private BitmapProcessNode bitmapProcessNode;

    public ImageTransform(BitmapProcessNode bitmapProcessNode) {
        super();
        this.bitmapProcessNode = bitmapProcessNode;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        } else if (bitmapProcessNode == null) {
            return toTransform;
        } else {
            return /*new ScaleBitmapNode(outWidth, outHeight).connect*/(bitmapProcessNode).processBitmap(toTransform);
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
