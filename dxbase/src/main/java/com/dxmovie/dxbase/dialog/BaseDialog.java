package com.dxmovie.dxbase.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.dxmovie.dxbase.R;
import com.dxmovie.dxbase.utils.KLog;
import com.dxmovie.dxbase.utils.PixelUtil;

/**
 * @desc baseDialog 已经做好了布局的量测，可以全屏
 */
public abstract class BaseDialog extends Dialog implements IBaseDialog {
    private Activity activity;
    //根布局
    private View rootView;
    //自定义View的容器
    private ViewGroup customViewGroup;
    //全屏布局容器
    private ViewGroup customFullScreenViewGroup;
    //正常布局
    private ViewGroup normalViewGroup;
    //自定义的布局View
    private View customView;

    public boolean isShow;

    public BaseDialog(Activity activity) {
        this(activity, R.style.dialog_base);
    }

    public BaseDialog(Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        setContentView(rootView);
    }

    /**
     * 初始化View 以及量测dialog的宽高
     */
    public void init() {
        setOwnerActivity(activity);
        setCanceledOnTouchOutside(isCancelOutside());
        setCancelable(isCancel());
        rootView = LayoutInflater.from(activity).inflate(initLayout(), null, false);
        customViewGroup = rootView.findViewById(R.id.custom_view);
        customFullScreenViewGroup = rootView.findViewById(R.id.full_screen_layout);
        normalViewGroup = rootView.findViewById(R.id.normal_layout);
        if (getCustomLayout() != -1) {
            normalViewGroup.setVisibility(View.GONE);
            customViewGroup.setVisibility(View.GONE);
            customFullScreenViewGroup.setVisibility(View.GONE);
            customViewGroup.removeAllViews();
            customFullScreenViewGroup.removeAllViews();
            customView = LayoutInflater.from(activity).inflate(getCustomLayout(), null, false);
            if (isFullScreen()) {
                customFullScreenViewGroup.addView(customView, 0);
                customFullScreenViewGroup.setVisibility(View.VISIBLE);
            } else {
                customViewGroup.addView(customView, 0);
                customViewGroup.setVisibility(View.VISIBLE);
            }
        }
        WindowManager.LayoutParams lay;
        DisplayMetrics dm;
        //dialog 的高度
        int height;
        //dialog 的宽度
        int weight;
        lay = getWindow().getAttributes();
        if (getAnimStyle() != 0) {
            getWindow().setWindowAnimations(getAnimStyle());
        } else {
            //不要默认动画
            getWindow().setWindowAnimations(R.style.nullStyle);
        }
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        if (!isFullScreen()) {
            int width = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int height1 = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            rootView.measure(width, height1);
            height = rootView.getMeasuredHeight();
            weight = dm.widthPixels - PixelUtil.dip2px(50);
        } else {
//            height = dm.heightPixels - rect.top;
            height = dm.heightPixels;
            weight = dm.widthPixels;
        }
        lay.height = height;
        lay.width = weight;
    }

    //改成抽象打印Log使用
    abstract protected String getDialogName();

    /**
     * 关闭dialog
     */
    @Override
    public void dismiss() {
        try {
            super.dismiss();
            isShow = false;
        } catch (Exception e) {
            KLog.e(getDialogName(), e);
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
     * 显示View 判断是否正在finish
     */
    @Override
    public void show() {
        if (activity != null) {
            if (activity.isFinishing()) {
                return;
            }
        }
        try {
            super.show();
            isShow = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    /**
     * 关闭输入法框
     */
    public void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null && getOwnerActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getOwnerActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 获取到跟布局
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    /**
     * 获取到自定义View
     *
     * @return
     */
    public View getCustomView() {
        return customView;
    }


    @Override
    public int initLayout() {
        return R.layout.dialog_common_layout;
    }

    @Override
    public boolean isCancel() {
        return true;
    }

    @Override
    public boolean isCancelOutside() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    public int getAnimStyle() {
        return 0;
    }
}
