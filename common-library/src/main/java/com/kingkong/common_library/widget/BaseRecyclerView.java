package com.kingkong.common_library.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class BaseRecyclerView extends RecyclerView {
    //滚动速度
    private double scale = 1.0;

    public BaseRecyclerView(@NonNull Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY *= scale;
        return super.fling(velocityX, velocityY);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }


    /**
     * 去除Item动画
     */
    public static void closeDefaultAnimator(RecyclerView mRecyclerView) {
        mRecyclerView.getItemAnimator().setAddDuration(0);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mRecyclerView.getItemAnimator().setMoveDuration(0);
        mRecyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
