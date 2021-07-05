package com.kingkong.common_library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dxmovie.dxbase.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.internal.InternalClassics;

public class LoadMoreFooterView extends FrameLayout implements RefreshFooter {


    private TextView tvText;
    private ProgressBar pbBar;

    protected boolean mNoMoreData = false;

    protected String mTextPulling = "上拉加载更多";
    protected String mTextLoading = "正在加载...";
    protected String mTextRefreshing = "正在刷新...";
    protected String mTextFinish = "加载完成";
    protected String mTextFailed = "加载失败";
    protected String mTextNothing = "没有了";


    public LoadMoreFooterView(@NonNull Context context) {
        this(context, null);
        View.inflate(context, R.layout.view_loadmore_footer, this);
        tvText = this.findViewById(R.id.ulfyAndroidTipTV);
        pbBar = this.findViewById(R.id.ulfyAndroidLoadingPB);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }


    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//        if (!mNoMoreData) {
//            super.onStartAnimator(refreshLayout, height, maxDragHeight);
//        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            tvText.setText(success ? mTextLoading : mTextFailed);
        }
        return 0;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                tvText.setText(mTextNothing);
                pbBar.setVisibility(GONE);
            } else {
                tvText.setText(mTextPulling);
                pbBar.setVisibility(VISIBLE);
            }
        }
        return true;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (!mNoMoreData) {

            switch (newState) {
                case PullUpToLoad:
                    pbBar.setVisibility(VISIBLE);
                    tvText.setText(mTextPulling);
                    break;
                case None:
                case Loading:
                case LoadReleased:
                    pbBar.setVisibility(VISIBLE);
                    tvText.setText(mTextLoading);
                    break;
                case ReleaseToLoad:
                    break;
                case Refreshing:
                    pbBar.setVisibility(GONE);
                    tvText.setText(mTextRefreshing);
                    break;
            }
        }
    }
}
