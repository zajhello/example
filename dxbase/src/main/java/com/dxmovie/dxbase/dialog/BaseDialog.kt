package com.dxmovie.dxbase.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.dxmovie.dxbase.R
import com.dxmovie.dxbase.utils.KLog
import com.dxmovie.dxbase.utils.PixelUtil

abstract class BaseDialog : Dialog, IBaseDialog {

    private var activity: Activity? = null
    //根布局
    private var rootView: View? = null
    //自定义View的容器
    private var customViewGroup: ViewGroup? = null
    //全屏布局容器
    private var customFullScreenViewGroup: ViewGroup? = null
    //正常布局
    private var normalViewGroup: ViewGroup? = null
    //自定义的布局View
    private var customView: View? = null

    var isShow = false

    constructor(context: Activity) : this(context, R.style.dialog_base)

    constructor(context: Activity, style: Int) : super(context, style) {
        activity = context
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRootView()
        initView()
        setContentView(rootView)
    }

    /**
     * 初始化View 以及量测dialog的宽高
     */
    private fun initRootView() {
        ownerActivity = activity
        setCanceledOnTouchOutside(isCancelOutside())
        setCancelable(isCancel())
        rootView = LayoutInflater.from(activity).inflate(initLayout(), null, false)
        customViewGroup = rootView?.findViewById(R.id.custom_view)
        customFullScreenViewGroup = rootView?.findViewById(R.id.full_screen_layout)
        normalViewGroup = rootView?.findViewById(R.id.normal_layout)
        if (getCustomLayout() != -1) {
            normalViewGroup?.visibility = View.GONE
            customViewGroup?.visibility = View.GONE
            customFullScreenViewGroup?.visibility = View.GONE
            customViewGroup?.removeAllViews()
            customFullScreenViewGroup?.removeAllViews()
            customView = LayoutInflater.from(activity).inflate(getCustomLayout(), null, false)
            if (isFullScreen()) {
                customFullScreenViewGroup?.addView(customView, 0)
                customFullScreenViewGroup?.visibility = View.VISIBLE
            } else {
                customViewGroup?.addView(customView, 0)
                customViewGroup?.visibility = View.VISIBLE
            }
        }
        //dialog 的高度
        val height: Int
        //dialog 的宽度
        val weight: Int
        val lay: WindowManager.LayoutParams = window.attributes
        if (getAnimStyle() != 0) {
            window.setWindowAnimations(getAnimStyle())
        } else { //不要默认动画
            window.setWindowAnimations(R.style.nullStyle)
        }
        val dm: DisplayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        val rect = Rect()
        activity!!.window.decorView.getWindowVisibleDisplayFrame(rect)
        if (!isFullScreen()) {
            val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val height1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            rootView?.measure(width, height1)
            height = rootView!!.measuredHeight
            weight = dm.widthPixels - PixelUtil.dip2px(50f)
        } else { //            height = dm.heightPixels - rect.top;
            height = dm.heightPixels
            weight = dm.widthPixels
        }
        lay.height = height
        lay.width = weight
    }


    //改成抽象打印Log使用
    protected abstract fun getDialogName(): String?

    /**
     * 关闭dialog
     */
    override fun dismiss() {
        try {
            super.dismiss()
            isShow = false
        } catch (e: Exception) {
            KLog.e(getDialogName(), e)
        }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
    }

    /**
     * 显示View 判断是否正在finish
     */
    override fun show() {
        if (activity != null) {
            if (activity!!.isFinishing) {
                return
            }
        }
        try {
            super.show()
            isShow = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return super.dispatchKeyEvent(event)
    }

    /**
     * 关闭输入法框
     */
    open fun hideKeyboard() {
        val v = this.currentFocus
        if (v != null && ownerActivity != null) {
            val imm = ownerActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    /**
     * 获取到跟布局
     *
     * @return
     */
    open fun getRootView(): View? {
        return rootView
    }

    override fun <T : View?> findViewById(id: Int): T {
        return rootView!!.findViewById<T>(id)
    }

    /**
     * 获取到自定义View
     *
     * @return
     */
    open fun getCustomView(): View? {
        return customView
    }


    override fun initLayout(): Int {
        return R.layout.dialog_common_layout
    }

    override fun isCancel(): Boolean {
        return true
    }

    override fun isCancelOutside(): Boolean {
        return true
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    open fun getAnimStyle(): Int {
        return 0
    }
}