package com.dxmovie.dxbase.widget

import android.R
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class StatusViewLayout : FrameLayout {

    companion object {

        val TAG: String = StatusViewLayout::class.java.simpleName

        /**
         * 加载中
         */
        const val STATUS_LOADING = 0
        /**
         * 加载错误
         */
        const val STATUS_ERROR = 1
        /**
         * 返回数据为空
         */
        const val STATUS_EMPTY = 2
        /**
         * 加载完成
         */
        const val STATUS_CONTENT = 3
        /**
         * 无网络
         */
        const val STATUS_NONETWORK = 4
    }

    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var mEmptyView: View? = null
    private var mNetWorkView: View? = null
    private var mLayoutParams: LayoutParams? = null
    private var mOnRetryListener: OnClickListener? = null
    // 空数据理论上不需要刷新,只有加载失败才要，为了不影响以前使用,在adapter中做了扩展
    private var mOnEmptyListener: OnClickListener? = null
    private var status_view_tv_error: TextView? = null
    private var status_tv_empty_msg: TextView? = null
    private var emptyMsg = "暂无数据"

    private val mAttr = intArrayOf(R.attr.id)

    fun setLoadingView(loadingView: View?) {
        if (mLoadingView != null) {
            removeView(mLoadingView)
        }
        addView(loadingView, 0, mLayoutParams)
        mLoadingView = loadingView
    }


    fun setmErrorView(erorView: View?) {
        if (mErrorView != null) {
            removeView(mErrorView)
        }
        addView(erorView, 0, mLayoutParams)
        mErrorView = erorView
        mErrorView!!.setOnClickListener { view ->
            if (mOnRetryListener != null) {
                showLoading()
                mOnRetryListener!!.onClick(view)
            }
        }
    }

    fun setmEmptyView(emptyView: View?) {
        if (mEmptyView != null) {
            removeView(mEmptyView)
        }
        addView(emptyView, 0, mLayoutParams)
        mEmptyView!!.setOnClickListener { view ->
            if (mOnEmptyListener != null) {
                showLoading()
                mOnEmptyListener!!.onClick(view)
            }
        }
        mEmptyView = emptyView
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val t = context.obtainStyledAttributes(attrs, com.dxmovie.dxbase.R.styleable.StatusViewLayout)
        var emptyLayout = t.getResourceId(com.dxmovie.dxbase.R.styleable.StatusViewLayout_sv_empty_layout, 0)
        var errorLayout = t.getResourceId(com.dxmovie.dxbase.R.styleable.StatusViewLayout_sv_error_layout, 0)
        var loadLayout = t.getResourceId(com.dxmovie.dxbase.R.styleable.StatusViewLayout_sv_load_layout, 0)
        var networkLayout = t.getResourceId(com.dxmovie.dxbase.R.styleable.StatusViewLayout_sv_network_layout, 0)

        if (emptyLayout == 0) {
            emptyLayout = com.dxmovie.dxbase.R.layout.status_view_layout_empty
        }
        if (errorLayout == 0) {
            errorLayout = com.dxmovie.dxbase.R.layout.status_view_layout_error
        }
        if (loadLayout == 0) {
            loadLayout = com.dxmovie.dxbase.R.layout.status_view_layout_load
        }
        if (networkLayout == 0) {
            networkLayout = com.dxmovie.dxbase.R.layout.status_view_layout_error
        }
        t.recycle()
        if (id == View.NO_ID) {
            id = com.dxmovie.dxbase.R.id.statusViewLayout
        }
        setUpView(context, emptyLayout, errorLayout, loadLayout, networkLayout)
    }


    private fun setUpView(context: Context, emptyLayout: Int, errorLayout: Int, loadLayout: Int, networkLayout: Int) {
        mLayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mLayoutParams?.gravity = Gravity.CENTER
        mLoadingView = LayoutInflater.from(getContext()).inflate(loadLayout, null)
        mErrorView = LayoutInflater.from(getContext()).inflate(errorLayout, null)
        mEmptyView = LayoutInflater.from(getContext()).inflate(emptyLayout, null)
        mNetWorkView = LayoutInflater.from(getContext()).inflate(networkLayout, null)
        status_view_tv_error = mErrorView?.findViewById(com.dxmovie.dxbase.R.id.warn)
        status_tv_empty_msg = mEmptyView?.findViewById(com.dxmovie.dxbase.R.id.warn)
        addView(mLoadingView, mLayoutParams)
        addView(mErrorView, mLayoutParams)
        addView(mEmptyView, mLayoutParams)
        addView(mNetWorkView, mLayoutParams)
        mEmptyView?.setOnClickListener(OnClickListener { view: View? ->
            if (mOnEmptyListener != null) {
                showLoading()
                mOnEmptyListener?.onClick(view)
            }
        })
        mErrorView?.setOnClickListener(OnClickListener { view: View? ->
            if (mOnRetryListener != null) {
                showLoading()
                mOnRetryListener?.onClick(view)
            }
        })
        showContent()
    }

    fun setOnRetryListener(listener: OnClickListener) {
        mOnRetryListener = listener
    }

    fun setEmptyListener(listener: OnClickListener) {
        mOnEmptyListener = listener
    }

    fun showLoading() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
        mLoadingView!!.visibility = View.VISIBLE
    }

    fun showError() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
        mErrorView!!.visibility = View.VISIBLE
    }

    fun showError(msg: String?) {
        showError()
        status_view_tv_error!!.text = msg
    }

    fun setEmptyText(text: String) {
        if (status_tv_empty_msg == null) {
            return
        }
        if (!TextUtils.isEmpty(text)) {
            emptyMsg = text
        }
        status_tv_empty_msg!!.text = text
    }

    fun showEmpty(msg: String) {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
        mEmptyView!!.visibility = View.VISIBLE
        setEmptyText(msg)
    }

    fun setEmptyIcon(iconId: Int) {
        if (status_tv_empty_msg == null) {
            return
        }
        if (iconId == -1) {
            status_tv_empty_msg!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            return
        }
        val drawable = ContextCompat.getDrawable(context, iconId)
        status_tv_empty_msg!!.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }


    fun showContent() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
        getChildAt(childCount - 1).visibility = View.VISIBLE
    }

    fun showNetWork() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
        mNetWorkView!!.visibility = View.VISIBLE
    }

    fun setStatus(status: Int) {
        when (status) {
            STATUS_LOADING -> showLoading()
            STATUS_EMPTY -> showEmpty(emptyMsg)
            STATUS_ERROR -> showError()
            STATUS_CONTENT -> showContent()
            STATUS_NONETWORK -> showNetWork()
            else -> {
            }
        }
    }

    fun getLoadingView(): View? {
        return mLoadingView
    }

    fun getErrorView(): View? {
        return mErrorView
    }

    fun getEmptyView(): View? {
        return mEmptyView
    }

    fun getNetWorkView(): View? {
        return mNetWorkView
    }

}