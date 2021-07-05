package com.dxmovie.dxbase.widget;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.dxmovie.dxbase.base.AppManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc
 */
public class LayoutManager {
    protected LayoutManager() {
    }

    /**
     * A {@link LinearLayoutManager}.
     */
    public static RecyclerView.LayoutManager linear() {
        return new LinearLayoutManager(AppManager.getsApplication());
    }

    /**
     * A {@link LinearLayoutManager} with the given orientation and reverseLayout.
     */
    public static RecyclerView.LayoutManager linear(@Orientation final int orientation) {
        return new LinearLayoutManager(AppManager.getsApplication(), orientation, false);
    }

    /**
     * A {@link GridLayoutManager} with the given spanCount.
     */
    public static RecyclerView.LayoutManager grid(final int spanCount) {
        return new GridLayoutManager(AppManager.getsApplication(), spanCount);

    }

    /**
     * A {@link GridLayoutManager} with the given spanCount.
     */
    public static RecyclerView.LayoutManager grid(final int spanCount, Boolean isScroll) {
        return new GridLayoutManager(AppManager.getsApplication(), spanCount) {
            @Override
            public boolean canScrollVertically() {
                return isScroll && super.canScrollVertically();
            }
        };

    }

    /**
     * A {@link GridLayoutManager} with the given spanCount, orientation and reverseLayout.
     **/
    public static RecyclerView.LayoutManager grid(final int spanCount, @Orientation final int orientation) {
        return new GridLayoutManager(AppManager.getsApplication(), spanCount, orientation, false);

    }

    /**
     * A {@link StaggeredGridLayoutManager} with the given spanCount and orientation.
     */
    public static RecyclerView.LayoutManager staggeredGrid(final int spanCount, @Orientation final int orientation) {
        return new StaggeredGridLayoutManager(spanCount, orientation);
    }

    @IntDef({LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }
}
