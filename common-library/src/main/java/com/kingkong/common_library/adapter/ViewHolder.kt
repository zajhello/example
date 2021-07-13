package com.kingkong.common_library.adapter

import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


    // 用来存放子View减少findViewById的次数
    private var mViews: SparseArray<View>? = null

    init {
        mViews = SparseArray()
    }

    /**
     * 设置TextView文本
     */
    fun setText(viewId: Int, text: CharSequence?): ViewHolder? {
        val tv: TextView = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    /**
     * 通过id获取view
     */
    fun <T : View?> getView(viewId: Int): T { // 先从缓存中找
        var view = mViews!![viewId]
        if (view == null) { // 直接从ItemView中找
            view = itemView.findViewById(viewId)
            mViews!!.put(viewId, view)
        }
        return view as T
    }


    /**
     * 设置View的Visibility
     */
    fun setViewVisibility(viewId: Int, visibility: Int): ViewHolder? {
        getView<View>(viewId).visibility = visibility
        return this
    }

    /**
     * 设置ImageView的资源
     */
    fun setImageResource(viewId: Int, resourceId: Int): ViewHolder? {
        val imageView = getView<ImageView>(viewId)
        imageView.setImageResource(resourceId)
        return this
    }

    /**
     * 设置条目点击事件
     */
    fun setOnIntemClickListener(listener: View.OnClickListener?) {
        itemView.setOnClickListener(listener)
    }

    /**
     * 设置条目长按事件
     */
    fun setOnIntemLongClickListener(listener: OnLongClickListener?) {
        itemView.setOnLongClickListener(listener)
    }

}