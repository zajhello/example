package com.kingkong.common_library.utils.glide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dxmovie.dxbase.utils.KLog;
import com.dxmovie.dxbase.utils.Utils;
import com.kingkong.common_library.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import io.reactivex.annotations.Nullable;


/**
 * Glide工具类
 */
public class GlideUtils {

    /**
     * @param context fix java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
     * @return
     */
    private static boolean isContextValidStatus(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }

    public static void loadImage(String url, ImageView imageView) {
        loadImageImpl(imageView.getContext(), url, 0, imageView, null);
    }

    public static void loadImage(Context mCtx, String url, int placeholder, ImageView imageView) {
        loadImageImpl(mCtx, url, placeholder, imageView, null);
    }

    public static void loadImage(String url, int placeholder, ImageView imageView) {
        loadImageImpl(imageView.getContext(), url, placeholder, imageView, null);
    }

    public static File loadFile(String imageUrl) throws ExecutionException, InterruptedException {
        RequestBuilder<File> builder = Glide
                .with(Utils.getContext())
                .asFile()
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(imageUrl);
        return builder.submit().get();
    }

    /***
     * 解决图片错乱问题
     * @param url
     * @param placeholder
     * @param imageView
     * @param imageTagKey  ImageView 打的 Tag的
     */
    public static void loadImage(String url, int placeholder, ImageView imageView, int imageTagKey) {
        if (imageTagKey > 0 && imageView != null && url != null) {
            Object obj = imageView.getTag(imageTagKey);
            if (obj != null && url.equals(String.valueOf(obj))) {
                loadImage(url, placeholder, imageView);
            } else {
                imageView.setImageResource(placeholder);
                imageView.setTag(imageTagKey, url);
            }
        }
    }

    public static void loadImage(String url, ImageView imageView, ImageTransform imageTransform) {
        loadImageImpl(imageView.getContext(), url, 0, imageView, imageTransform);
    }

    public static void loadImage(String url, int placeholder, ImageView imageView, ImageTransform imageTransform) {
        loadImageImpl(imageView.getContext(), url, placeholder, imageView, imageTransform);
    }

    /**
     * 加载本地图片
     *
     * @param file
     * @param placeholder
     * @param imageView
     */
    public static void loadImage(File file, int placeholder, ImageView imageView) {
        loadImageImpl(imageView.getContext(), file, placeholder, imageView, null);
    }

    public static void clearImage(ImageView imageView) {
        if (imageView != null) {
            Glide.with(imageView.getContext()).clear(imageView);
        }
    }

    private static void loadImageImpl(final Context ctx, final String url, final int placeholder, final ImageView imageView, final ImageTransform imageTransform) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (isContextValidStatus(ctx)) {
                    int width = imageView.getWidth();
                    int height = imageView.getHeight();
                    // LogUtils.log(String.format("加载的图片宽高为===== width = %s height = %s", width, height));
                    // loadImageImplForSize(ctx, url, placeholder, imageView, imageTransform, width, height);
                    loadImageImplFor(ctx, url, placeholder, imageView, imageTransform, width, height);
                } else {
                    KLog.i("glide load pics but context is null or destroyed");
                }
            }
        });
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
    private static void loadImageImpl(final Context ctx, final File file, final int placeholder, final ImageView imageView, final ImageTransform imageTransform) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (isContextValidStatus(ctx)) {
                    int width = imageView.getWidth();
                    int height = imageView.getHeight();
                    // LogUtils.log(String.format("加载的图片宽高为===== width = %s height = %s", width, height));
                    // loadImageImplForSize(ctx, url, placeholder, imageView, imageTransform, width, height);
                    loadLocalImageImplFor(ctx, file, placeholder, imageView, imageTransform, width, height);
                } else {
                    KLog.i("glide load pics but context is null or destroyed");
                }
            }
        });
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
    private static void loadImageImplForSize(Context ctx, String url, int placeholder, final ImageView imageView, ImageTransform imageTransform, int width, int height) {
        // 如果路径为空，则直接显示占位图
        if (TextUtils.isEmpty(url) || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder);
            }
            return;
        }

        ctx = ctx.getApplicationContext();
        RequestBuilder<Drawable> load = Glide.with(ctx).load(url);
        if (placeholder <= 0) {
            load.placeholder(imageView.getDrawable());
        } else {
            load.placeholder(placeholder);
            load.error(placeholder);
        }
        if (imageTransform != null) {
            load.transform(imageTransform);
        }
        if (width > 0 && height > 0) {
            load.override(width, height);
        }
        // 禁用加载过渡动画，否则scaleType=centerCrop时会出现抖动
        load.dontAnimate();
        load.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        load.into(imageView);
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
    private static void loadImageImplFor(Context ctx, String url, int placeholder, final ImageView imageView, ImageTransform imageTransform, int width, int height) {
        // 如果路径为空，则直接显示占位图
        if (TextUtils.isEmpty(url) || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder);
            }
            return;
        }
        ctx = ctx.getApplicationContext();
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        if (imageTransform != null) {
            requestOptions.transform(imageTransform);
        }
        if (width > 0 && height > 0) {
            requestOptions.override(width, height);
        }
        Glide.with(ctx).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).apply(requestOptions).into(imageView);
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
    private static void loadLocalImageImplFor(Context ctx, File file, int placeholder, final ImageView imageView, ImageTransform imageTransform, int width, int height) {
        // 如果路径为空，则直接显示占位图
        if (file == null || !isContextValidStatus(ctx)) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder);
            }
            return;
        }
        ctx = ctx.getApplicationContext();
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (imageTransform != null) {
            requestOptions.transform(imageTransform);
        }
        if (width > 0 && height > 0) {
            requestOptions.override(width, height);
        }
        Glide.with(ctx).load(file).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).apply(requestOptions).into(imageView);
    }

    public static void loadGifImage(ImageView imageView) {
        loadGifImage(imageView, 0);
    }

    public static void loadGifImage(ImageView imageView, int defaultImg) {
        loadGifImage(imageView, defaultImg, 0);
    }

    /***
     * 加载 git 格式图
     * @param imageView
     * @param defaultImg  默认图片
     * @param placeholder
     */
    public static void loadGifImage(ImageView imageView, int defaultImg, int placeholder) {
        if (imageView == null) return;
        if (defaultImg == 0) {
            defaultImg = R.drawable.loading;
        }
        if (placeholder == 0) {
            placeholder = R.drawable.loading;
        }
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext()).load(defaultImg).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).apply(requestOptions).into(imageView);

    }

    /***
     * Url 加载的图片
     * @param context
     * @param imageView
     * @param url
     * @param placeholder
     */
    public static void loadGifImage(Context context, ImageView imageView, String url, int placeholder) {
        if (imageView == null) return;
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(requestOptions).into(imageView);
    }

}
