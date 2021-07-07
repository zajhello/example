package com.dxmovie.dxbase.widget

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.dxmovie.dxbase.R
import java.lang.ref.SoftReference

class LoadingDialog constructor(activity: Activity) : Dialog(activity, R.style.transparent_dialog) {

    var activity : Activity? = null
        get() = mOwnerActivitySoft?.get()

    // 全局的loading 黑框宽度
    var width = -1
    // 全局的loading 黑框高度
    var height = -1

    private var mOwnerActivitySoft: SoftReference<Activity>? = null

    init {
        initView()
        mOwnerActivitySoft = SoftReference(activity)
    }

//    fun getActivity(): Activity? {
//        return if (mOwnerActivitySoft == null) {
//            null
//        } else mOwnerActivitySoft!!.get()
//    }

    fun initView() {
        setContentView(R.layout.dialog_loading_layout)
        window.setWindowAnimations(R.style.nullStyle)
        val llContent = findViewById<View>(R.id.ll_content)
        if (height != -1) {
            val layoutParams = llContent.layoutParams
            layoutParams.height = height
        }
        if (width != -1) {
            val layoutParams = llContent.layoutParams
            layoutParams.width = width
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * @param type
     * @see TYPE
     */
    fun setDissmissType(type: Int) {
        if (type == TYPE.WEAK) {
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        } else if (type == TYPE.SOFT) {
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        } else {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
    }

    /**
     * 弱的模式，点击外部和返回都消失
     */
    fun cancelable() {
        setDissmissType(TYPE.WEAK)
    }

    /**
     * 软的模式，点击外部不消失，只点击返回键消失
     */
    fun softCancelable() {
        setDissmissType(TYPE.SOFT)
    }

    /**
     * 只能代码调用消失
     */
    fun nonCancelable() {
        setDissmissType(TYPE.HARD)
    }

    object TYPE {
        /**
         * 弱的模式，点击外部和返回都消失
         */
        var WEAK = 0
        /**
         * 软的模式，点击外部不消失，只点击返回键消失
         */
        var SOFT = 1
        /**
         * 只能代码调用消失
         */
        var HARD = 2
    }
}