package com.dxmovie.dxbase.widget

import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dxmovie.dxbase.base.AppManager
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class LayoutManager constructor() {

    companion object {
        /**
         * A [LinearLayoutManager].
         */
        @JvmStatic
        fun linear(): RecyclerView.LayoutManager? {
            return LinearLayoutManager(AppManager.sApplication)
        }

        /**
         * A [LinearLayoutManager] with the given orientation and reverseLayout.
         */
        @JvmStatic
        fun linear(@Orientation orientation: Int): RecyclerView.LayoutManager? {
            return LinearLayoutManager(AppManager.sApplication, orientation, false)
        }

        /**
         * A [GridLayoutManager] with the given spanCount.
         */
        @JvmStatic
        fun grid(spanCount: Int): RecyclerView.LayoutManager? {
            return GridLayoutManager(AppManager.sApplication, spanCount)
        }

        /**
         * A [GridLayoutManager] with the given spanCount.
         */
        @JvmStatic
        fun grid(spanCount: Int, isScroll: Boolean): RecyclerView.LayoutManager? {
            return object : GridLayoutManager(AppManager.sApplication, spanCount) {
                override fun canScrollVertically(): Boolean {
                    return isScroll && super.canScrollVertically()
                }
            }
        }

        /**
         * A [GridLayoutManager] with the given spanCount, orientation and reverseLayout.
         */
        @JvmStatic
        fun grid(spanCount: Int, @Orientation orientation: Int): RecyclerView.LayoutManager? {
            return GridLayoutManager(AppManager.sApplication, spanCount, orientation, false)
        }

        /**
         * A [StaggeredGridLayoutManager] with the given spanCount and orientation.
         */
        @JvmStatic
        fun staggeredGrid(spanCount: Int, @Orientation orientation: Int): RecyclerView.LayoutManager? {
            return StaggeredGridLayoutManager(spanCount, orientation)
        }
    }

    @IntDef(LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Orientation

}