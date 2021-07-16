package com.kingkong.common_library.utils.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dxmovie.dxbase.utils.KLog
import com.dxmovie.dxbase.utils.Utils.getContext
import com.kingkong.common_library.R
import java.io.File
import java.util.concurrent.ExecutionException

/**
 * Glide工具类
 */
object GlideUtils {

    /**
     * @param context fix java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
     * @return
     */
    private fun isContextValidStatus(context: Context): Boolean {
        return context is Activity && !context.isFinishing
    }

    fun loadImage(url: String, imageView: ImageView) {
        loadImageImpl(imageView.context, url, 0, imageView, null)
    }

    @JvmStatic
    fun loadImage(mCtx: Context, url: String, placeholder: Int, imageView: ImageView) {
        loadImageImpl(mCtx, url, placeholder, imageView, null)
    }

    fun loadImage(url: String, placeholder: Int, imageView: ImageView) {
        loadImageImpl(imageView.context, url, placeholder, imageView, null)
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun loadFile(imageUrl: String?): File? {
        val builder = Glide
                .with(getContext()!!)
                .asFile()
                .apply(RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(imageUrl)
        return builder.submit().get()
    }

    /***
     * 解决图片错乱问题
     * @param url
     * @param placeholder
     * @param imageView
     * @param imageTagKey  ImageView 打的 Tag的
     */
    fun loadImage(url: String?, placeholder: Int, imageView: ImageView?, imageTagKey: Int) {
        if (imageTagKey > 0 && imageView != null && url != null) {
            val obj = imageView.getTag(imageTagKey)
            if (obj != null && url == obj.toString()) {
                loadImage(url, placeholder, imageView)
            } else {
                imageView.setImageResource(placeholder)
                imageView.setTag(imageTagKey, url)
            }
        }
    }

    fun loadImage(url: String, imageView: ImageView, imageTransform: ImageTransform?) {
        loadImageImpl(imageView.context, url, 0, imageView, imageTransform)
    }

    fun loadImage(url: String, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?) {
        loadImageImpl(imageView.context, url, placeholder, imageView, imageTransform)
    }

    /**
     * 加载本地图片
     *
     * @param file
     * @param placeholder
     * @param imageView
     */
    fun loadImage(file: File, placeholder: Int, imageView: ImageView) {
        loadImageImpl(imageView.context, file, placeholder, imageView, null)
    }

    fun clearImage(imageView: ImageView?) {
        if (imageView != null) {
            Glide.with(imageView.context).clear(imageView)
        }
    }

    private fun loadImageImpl(ctx: Context, url: String, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?) {
        imageView.post {
            if (isContextValidStatus(ctx)) {
                val width = imageView.width
                val height = imageView.height
                // LogUtils.log(String.format("加载的图片宽高为===== width = %s height = %s", width, height));
                // loadImageImplForSize(ctx, url, placeholder, imageView, imageTransform, width, height);
                loadImageImplFor(ctx, url, placeholder, imageView, imageTransform, width, height)
            } else {
                KLog.i("glide load pics but context is null or destroyed")
            }
        }
    }

    /**
     * 加载本地图片
     *
     * @param ctx
     * @param file
     * @param placeholder
     * @param imageView
     * @param imageTransform
     */
    private fun loadImageImpl(ctx: Context, file: File, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?) {
        imageView.post {
            if (isContextValidStatus(ctx)) {
                val width = imageView.width
                val height = imageView.height
                // LogUtils.log(String.format("加载的图片宽高为===== width = %s height = %s", width, height));
                // loadImageImplForSize(ctx, url, placeholder, imageView, imageTransform, width, height);
                loadLocalImageImplFor(ctx, file, placeholder, imageView, imageTransform, width, height)
            } else {
                KLog.i("glide load pics but context is null or destroyed")
            }
        }
    }

    /**
     * 服务方法，供其他方法调用
     *
     * @param ctx            上下文
     * @param url            加载地址
     * @param placeholder    占位符
     * @param imageView      目标
     * @param imageTransform 图片转换
     */
    @SuppressLint("CheckResult")
    private fun loadImageImplForSize(ctx: Context, url: String, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?, width: Int, height: Int) {
        // 如果路径为空，则直接显示占位图
        var ctx = ctx
        if (TextUtils.isEmpty(url) || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder)
            }
            return
        }
        ctx = ctx.applicationContext
        val load = Glide.with(ctx).load(url)
        if (placeholder <= 0) {
            load.placeholder(imageView.drawable)
        } else {
            load.placeholder(placeholder)
            load.error(placeholder)
        }
        if (imageTransform != null) {
            load.transform(imageTransform)
        }
        if (width > 0 && height > 0) {
            load.override(width, height)
        }
        // 禁用加载过渡动画，否则scaleType=centerCrop时会出现抖动
        load.dontAnimate()
        load.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        load.into(imageView)
    }

    /**
     * 加载网络图片
     *
     * @param ctx
     * @param url
     * @param placeholder
     * @param imageView
     * @param imageTransform
     * @param width
     * @param height
     */
    @SuppressLint("CheckResult")
    private fun loadImageImplFor(ctx: Context, url: String, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?, width: Int, height: Int) {
        // 如果路径为空，则直接显示占位图
        var ctx = ctx
        if (TextUtils.isEmpty(url) || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder)
            }
            return
        }
        ctx = ctx.applicationContext
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        if (imageTransform != null) {
            requestOptions.transform(imageTransform)
        }
        if (width > 0 && height > 0) {
            requestOptions.override(width, height)
        }
        Glide.with(ctx).load(url).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                if (resource is GifDrawable) {
                    resource.setLoopCount(1)
                }
                return false
            }
        }).apply(requestOptions).into(imageView)
    }

    /**
     * 加载本地图片
     *
     * @param ctx
     * @param file
     * @param placeholder
     * @param imageView
     * @param imageTransform
     * @param width
     * @param height
     */
    @SuppressLint("CheckResult")
    private fun loadLocalImageImplFor(ctx: Context, file: File?, placeholder: Int, imageView: ImageView, imageTransform: ImageTransform?, width: Int, height: Int) {
        // 如果路径为空，则直接显示占位图
        var ctx = ctx
        if (file == null || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder)
            }
            return
        }
        ctx = ctx.applicationContext
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        if (imageTransform != null) {
            requestOptions.transform(imageTransform)
        }
        if (width > 0 && height > 0) {
            requestOptions.override(width, height)
        }
        Glide.with(ctx).load(file).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                if (resource is GifDrawable) {
                    resource.setLoopCount(1)
                }
                return false
            }
        }).apply(requestOptions).into(imageView)
    }

    fun loadGifImage(imageView: ImageView?) {
        loadGifImage(imageView, 0)
    }

    fun loadGifImage(imageView: ImageView?, defaultImg: Int) {
        loadGifImage(imageView, defaultImg, 0)
    }

    /***
     * 加载 git 格式图
     * @param imageView
     * @param defaultImg  默认图片
     * @param placeholder
     */
    fun loadGifImage(imageView: ImageView?, defaultImg: Int, placeholder: Int) {
        var defaultImg = defaultImg
        var placeholder = placeholder
        if (imageView == null) return
        if (defaultImg == 0) {
            defaultImg = R.drawable.loading
        }
        if (placeholder == 0) {
            placeholder = R.drawable.loading
        }
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(imageView.context).load(defaultImg).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                if (resource is GifDrawable) {
                    resource.setLoopCount(1)
                }
                return false
            }
        }).apply(requestOptions).into(imageView)
    }

    /***
     * Url 加载的图片
     * @param context
     * @param imageView
     * @param url
     * @param placeholder
     */
    fun loadGifImage(context: Context?, imageView: ImageView?, url: String?, placeholder: Int) {
        if (imageView == null) return
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!).load(url).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }).apply(requestOptions).into(imageView)
    }
}