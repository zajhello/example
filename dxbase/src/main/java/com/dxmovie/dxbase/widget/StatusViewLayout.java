package com.dxmovie.dxbase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dxmovie.dxbase.R;

/**
 * @Description 自定义状态View
 */
public class StatusViewLayout extends FrameLayout {

    public static final String TAG = StatusViewLayout.class.getSimpleName();

    /**
     * 加载中
     */
    public static final int STATUS_LOADING = 0;
    /**
     * 加载错误
     */
    public static final int STATUS_ERROR = 1;
    /**
     * 返回数据为空
     */
    public static final int STATUS_EMPTY = 2;
    /**
     * 加载完成
     */
    public static final int STATUS_CONTENT = 3;
    /**
     * 无网络
     */
    public static final int STATUS_NONETWORK = 4;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNetWorkView;
    private LayoutParams mLayoutParams;
    private OnClickListener mOnRetryListener;
    // 空数据理论上不需要刷新,只有加载失败才要，为了不影响以前使用,在adapter中做了扩展
    private OnClickListener mOnEmptyListener;
    private TextView status_view_tv_error;
    private TextView status_tv_empty_msg;
    private String emptyMsg = "暂无数据";

    private final int[] mAttr = {android.R.attr.id};

    public void setLoadingView(View loadingView) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        addView(loadingView, 0, mLayoutParams);
        this.mLoadingView = loadingView;
    }


    public void setmErrorView(View erorView) {
        if (mErrorView != null) {
            removeView(mErrorView);
        }
        addView(erorView, 0, mLayoutParams);
        this.mErrorView = erorView;
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnRetryListener != null) {
                    showLoading();
                    mOnRetryListener.onClick(view);
                }
            }
        });
    }

    public void setmEmptyView(View emptyView) {
        if (mEmptyView != null) {
            removeView(mEmptyView);
        }
        addView(emptyView, 0, mLayoutParams);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEmptyListener != null) {
                    showLoading();
                    mOnEmptyListener.onClick(view);
                }
            }
        });
        this.mEmptyView = emptyView;
    }

    public StatusViewLayout(Context context) {
        this(context, null);
    }

    public StatusViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.StatusViewLayout);
        int emptyLayout = t.getResourceId(R.styleable.StatusViewLayout_sv_empty_layout, 0);
        int errorLayout = t.getResourceId(R.styleable.StatusViewLayout_sv_error_layout, 0);
        int loadLayout = t.getResourceId(R.styleable.StatusViewLayout_sv_load_layout, 0);
        int networkLayout = t.getResourceId(R.styleable.StatusViewLayout_sv_network_layout, 0);

        if (emptyLayout == 0) {
            emptyLayout = R.layout.status_view_layout_empty;
        }
        if (errorLayout == 0) {
            errorLayout = R.layout.status_view_layout_error;
        }
        if (loadLayout == 0) {
            loadLayout = R.layout.status_view_layout_load;
        }
        if (networkLayout == 0) {
            networkLayout = R.layout.status_view_layout_error;
        }
        t.recycle();
        if (getId() == NO_ID) {
            setId(R.id.statusViewLayout);
        }
        setUpView(context, emptyLayout, errorLayout, loadLayout, networkLayout);

    }

    private void setUpView(Context context, int emptyLayout, int errorLayout, int loadLayout, int networkLayout) {

        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.gravity = Gravity.CENTER;

        mLoadingView = LayoutInflater.from(getContext()).inflate(loadLayout, null);
        mErrorView = LayoutInflater.from(getContext()).inflate(errorLayout, null);
        mEmptyView = LayoutInflater.from(getContext()).inflate(emptyLayout, null);
        mNetWorkView = LayoutInflater.from(getContext()).inflate(networkLayout, null);
        status_view_tv_error = mErrorView.findViewById(R.id.warn);
        status_tv_empty_msg = mEmptyView.findViewById(R.id.warn);
        addView(mLoadingView, mLayoutParams);
        addView(mErrorView, mLayoutParams);
        addView(mEmptyView, mLayoutParams);
        addView(mNetWorkView, mLayoutParams);
        mEmptyView.setOnClickListener(view -> {
            if (mOnEmptyListener != null) {
                showLoading();
                mOnEmptyListener.onClick(view);
            }
        });
        mErrorView.setOnClickListener(view -> {
            if (mOnRetryListener != null) {
                showLoading();
                mOnRetryListener.onClick(view);
            }
        });
        showContent();
    }

    public void setOnRetryListener(OnClickListener listener) {
        mOnRetryListener = listener;
    }

    public void setEmptyListener(OnClickListener listener) {
        mOnEmptyListener = listener;
    }

    public void showLoading() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void showError() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void showError(String msg) {
        showError();
        status_view_tv_error.setText(msg);
    }

    public void setEmptyText(String text) {
        if (status_tv_empty_msg == null) {
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            emptyMsg = text;
        }
        status_tv_empty_msg.setText(text);
    }

    public void showEmpty(String msg) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mEmptyView.setVisibility(View.VISIBLE);
        setEmptyText(msg);
    }

    public void setEmptyIcon(int iconId) {
        if (status_tv_empty_msg == null) {
            return;
        }
        if (iconId == -1) {
            status_tv_empty_msg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), iconId);
        status_tv_empty_msg.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }


    public void showContent() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        getChildAt(getChildCount() - 1).setVisibility(View.VISIBLE);
    }

    public void showNetWork() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mNetWorkView.setVisibility(View.VISIBLE);
    }

    public void setStatus(int status) {
        switch (status) {
            case STATUS_LOADING:
                showLoading();
                break;
            case STATUS_EMPTY:
                showEmpty(emptyMsg);
                break;
            case STATUS_ERROR:
                showError();
                break;
            case STATUS_CONTENT:
                showContent();
                break;
            case STATUS_NONETWORK:
                showNetWork();
                break;
            default:
                break;
        }
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getNetWorkView() {
        return mNetWorkView;
    }

}
