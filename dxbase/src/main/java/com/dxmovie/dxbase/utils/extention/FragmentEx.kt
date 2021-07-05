package com.dxmovie.dxbase.utils.extention

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 *@desc Fragment 扩展
 */

/** Fragment关闭自己 */
fun Fragment.dismiss() {
    this.activity?.apply {
        if (isDestroyed || isFinishing) {
            return
        }
        removeFragment(this@dismiss)
    }
}

/** 当前Fragment 移除堆栈中的其他Fragment */
fun Fragment.removeFragment(fragment: Fragment) {
    activity?.apply {
        if (isDestroyed || isFinishing) {
            return
        }
        removeFragment(fragment)
    }
}

/** 实例化一个Fragment */
inline fun <reified F : Fragment> Context.newFragment(vararg args: Pair<String, String>): F {
    val bundle = Bundle()
    args.let {
        for (arg in args) {
            bundle.putString(arg.first, arg.second)
        }
    }
    @Suppress("DEPRECATION")
    return Fragment.instantiate(this, F::class.java.name, bundle) as F
}

