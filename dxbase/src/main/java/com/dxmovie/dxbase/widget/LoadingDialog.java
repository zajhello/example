package com.dxmovie.dxbase.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import com.dxmovie.dxbase.R;

import java.lang.ref.SoftReference;

/**
 * <p>
 * globeAnimateSrc 用于整个app全局的动画资源 一般需要在application 配置:LoadingDialog.globeAnimateSrc = xxxx.json
 * defaultAnimateSrc 当前面两个资源都没有设置的时候，用默认资源
 * 三个的优先级 animateSrc > globeAnimateSrc > defaultAnimateSrc
 */
public class LoadingDialog extends Dialog {

    // 全局的loading 黑框宽度
    public static int width = -1;
    // 全局的loading 黑框高度
    public static int height = -1;

    private SoftReference<Activity> mOwnerActivitySoft;

    public LoadingDialog(Activity activity) {
        super(activity, R.style.transparent_dialog);
        initView();
        mOwnerActivitySoft = new SoftReference<>(activity);
    }


    public Activity getActivity() {
        if (mOwnerActivitySoft == null) {
            return null;
        }
        return mOwnerActivitySoft.get();
    }

    public void initView() {
        setContentView(R.layout.dialog_loading_layout);
        getWindow().setWindowAnimations(R.style.nullStyle);
        View llContent = findViewById(R.id.ll_content);

        if (height != -1) {
            ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
            layoutParams.height = height;
        }

        if (width != -1) {
            ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
            layoutParams.width = width;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * @param type
     * @see TYPE
     */
    public void setDissmissType(int type) {
        if (type == TYPE.WEAK) {
            setCanceledOnTouchOutside(true);
            setCancelable(true);
        } else if (type == TYPE.SOFT) {
            setCanceledOnTouchOutside(false);
            setCancelable(true);
        } else {
            setCanceledOnTouchOutside(false);
            setCancelable(false);
        }
    }

    /**
     * 弱的模式，点击外部和返回都消失
     */
    public void cancelable() {
        setDissmissType(TYPE.WEAK);
    }

    /**
     * 软的模式，点击外部不消失，只点击返回键消失
     */
    public void softCancelable() {
        setDissmissType(TYPE.SOFT);
    }

    /**
     * 只能代码调用消失
     */
    public void nonCancelable() {
        setDissmissType(TYPE.HARD);
    }

    public static class TYPE {
        /**
         * 弱的模式，点击外部和返回都消失
         */
        public static int WEAK = 0;
        /**
         * 软的模式，点击外部不消失，只点击返回键消失
         */
        public static int SOFT = 1;
        /**
         * 只能代码调用消失
         */
        public static int HARD = 2;
    }
}
