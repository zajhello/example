package com.kingkong.common_library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class CommonRecyclerAdapter<T> constructor(context: Context) : RecyclerView.Adapter<ViewHolder>() {

    protected var mContext: Context? = null
    protected var mInflater: LayoutInflater? = null

    //数据怎么办？
    protected var mData: List<T>
    private var mLayoutId = 0
    private var page: Page? = null

    // 多布局支持
    private var mMultiTypeSupport: MultiTypeSupport<T>? = null

    init {
        mContext = context
        mInflater = LayoutInflater.from(mContext)
        mData = ArrayList()
        mLayoutId = -1
    }

    fun setMultiTypeSupport(multiTypeSupport: MultiTypeSupport<T>) {
        this.mMultiTypeSupport = multiTypeSupport
    }

    fun setLayoutId(layoutId: Int) {
        mLayoutId = layoutId
    }

    fun getPage(): Page? {
        if (page == null) {
            page = Page()
        }
        return page
    }

    /**
     * 增加Item
     *
     * @param t
     */
    fun add(index: Int, t: T) {
        mData.toMutableList().add(index, t)
        notifyItemInserted(index)
    }

    /**
     * 替换Item
     *
     * @param t
     */
    fun replace(index: Int, t: T) {
        mData.toMutableList()[index] = t
        notifyItemChanged(index)
    }

    /**
     * 增加Item
     *
     * @param t
     */
    fun add(t: T?) {
        if (t != null) {
            mData.toMutableList().add(t)
            notifyItemInserted(mData.size - 1)
        }
    }

    /**
     * 增加List、
     *
     * @param list
     */
    fun addAll(list: List<T>) {
        if (list != null && list.isNotEmpty()) {
            mData.toMutableList().addAll(list)
            notifyItemRangeInserted(mData.size - list.size, list.size)
        }
    }

    fun autoAddAll(list: List<T>) {
        if (getPage()!!.page == 1) {
            setList(list)
        } else {
            addAll(list)
        }
    }

    /**
     * 移除item
     *
     * @param t
     * @return
     */
    fun remove(t: T): Boolean {
        val remove: Boolean = mData.toMutableList().remove(t)
        notifyDataSetChanged()
        return remove
    }

    fun remove(index: Int) {
        if (index < 0 || mData.size < index + 1) return
        mData.toMutableList() .removeAt(index)
        notifyItemRemoved(index)
    }

    /**
     * 移除list
     *
     * @param list
     * @return
     */
    fun removeAll(list: List<T>): Boolean {
        val b: Boolean = mData.toMutableList().removeAll(list)
        notifyDataSetChanged()
        return b
    }

    /**
     * 获取List
     *
     * @return
     */
    fun getList(): List<T>? {
        return mData
    }


    /**
     * 设置List
     *
     * @param list
     */
    fun setList(list: List<T>) {
        mData.toMutableList().clear()
        mData.toMutableList().addAll(list)
        notifyDataSetChanged()
    }


    /**
     * 刷新数据
     *
     * @param list
     */
    fun refresh(list: List<T>?) {
        setList(list!!)
    }

    /**
     * 加载更多
     *
     * @param list
     */
    fun loadMore(list: List<T>?) {
        addAll(list!!)
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    override fun getItemViewType(position: Int): Int {
        // 多布局支持
        return if (mMultiTypeSupport != null) {
            mMultiTypeSupport!!.getLayoutId(mData[position], position)
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType
        }
        // 先inflate数据
        val itemView = mInflater!!.inflate(mLayoutId, parent, false)
        // 返回ViewHolder
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener { v -> mItemClickListener!!.onItemClick(v, position) }
            holder.itemView.setOnLongClickListener { v -> mItemClickListener!!.onItemLongClick(v, position) }
        }
        convert(holder, position, mData[position])
    }


    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    abstract fun convert(holder: ViewHolder, position: Int, item: T)

    override fun getItemCount(): Int {
        return mData.size
    }

    /***************
     * 设置条目点击和长按事件
     */
    var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }


    class Page {
        constructor() {
            page = 1
            size = 20
        }

        constructor(page: Int, size: Int) {
            this.page = page
            this.size = size
        }

        var page: Int
        var size: Int

        operator fun next() {
            page++
        }

        fun reset() {
            page = 1
        }
    }

}