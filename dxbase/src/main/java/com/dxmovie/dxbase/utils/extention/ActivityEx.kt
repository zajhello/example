package com.dxmovie.dxbase.utils.extention

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.jetbrains.annotations.NotNull

/**
 *@desc Activity、Context 扩展
 */

/** 屏幕的宽度 */
fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/**
 * 屏幕的高度
 */
fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

/** 移除Fragment */
fun FragmentActivity.removeFragment(@NotNull fragment: Fragment) {
    if (isDestroyed || isFinishing) {
        return
    }
    supportFragmentManager.apply {
        beginTransaction()
                .remove(fragment)
                .commitNowAllowingStateLoss()
    }
}

/** 开启一个新的Activity */
inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.apply {
        intent.putExtras(this)
    }
    startActivity(intent)
}


fun Activity?.isExist(@NonNull packageName: String, @NonNull className: String): Boolean {
    val intent = Intent()
    intent.setClassName(packageName, className)
    this?.let {
        val isExist = packageManager.resolveActivity(intent, 0) != null
        return isExist
    }
    return false
}

fun Context?.isXActivityAlive(@NonNull activityName: String): Boolean {
    this?.let {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = am.getRunningTasks(20)
        runningTasks.forEach {
            val topActivityStr = it.topActivity?.className
            val baseActivityStr = it.baseActivity?.className
            if (topActivityStr.isNullOrEmpty() || baseActivityStr.isNullOrEmpty()) return false
            if (topActivityStr == activityName || baseActivityStr == activityName) return true
        }
    }
    return false
}
