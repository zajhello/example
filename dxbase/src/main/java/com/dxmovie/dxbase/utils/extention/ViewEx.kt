package com.dxmovie.dxbase.utils.extention

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/* ----------view  显示隐藏---------- */
fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

/* ----------view  显示隐藏---------- */


fun getRealContext(view: View): FragmentActivity? {
    var context = view.context
    while (context is android.content.ContextWrapper) {
        if (context is FragmentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.realContext() = getRealContext(this)

inline fun View.doOnLayoutAvailable(crossinline block: () -> Unit) {
    //如果 view 已经通过至少一个布局，则返回true，因为它最后一次附加到窗口或从窗口分离。
    ViewCompat.isLaidOut(this)
            .yes {
                block()
            }
            .otherwise {
                addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                            v: View?,
                            left: Int,
                            top: Int,
                            right: Int,
                            bottom: Int,
                            oldLeft: Int,
                            oldTop: Int,
                            oldRight: Int,
                            oldBottom: Int
                    ) {
                        removeOnLayoutChangeListener(this)
                        block()
                    }
                })
            }
}

fun View.paddingAll(padding: Int) {
    this.setPadding(padding, padding, padding, padding)
}

fun View.setPaddingLeft(padding: Int) {
    this.setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setPaddingRight(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setPaddingTop(padding: Int) {
    this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun EditText.setCompoundDrawablesWithIntrinsicBoundsLeft(@DrawableRes left: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(context, left), null, null, null
    )
}

fun View.setPaddingBottom(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun View.setWidth(width: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.height = height
    layoutParams = params
}

fun View.setSize(
        width: Int,
        height: Int
) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    params.height = height
    layoutParams = params
}

/*------------------------------------------ViewGroup------------------------------------------*/
inline val ViewGroup.views get() = (0 until childCount).map { getChildAt(it) }

/*------------------------------------------TextView------------------------------------------*/
inline fun TextView.textWatcher(init: KTextWatcher.() -> Unit) = addTextChangedListener(KTextWatcher().apply(init))

class KTextWatcher : TextWatcher {

    val TextView.isEmpty
        get() = text.isEmpty()

    val TextView.isNotEmpty
        get() = text.isNotEmpty()

    val TextView.isBlank
        get() = text.isBlank()

    val TextView.isNotBlank
        get() = text.isNotBlank()

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((Editable?) -> Unit)? = null

    override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
    ) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
    ) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    fun afterTextChanged(listener: (Editable?) -> Unit) {
        _afterTextChanged = listener
    }
}

fun TextView.topDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

fun TextView.leftDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView.rightDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView.bottomDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}

fun TextView.topDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(
                compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3]
        )
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }
}

fun TextView.leftDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(
                drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]
        )
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}

fun TextView.rightDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(
                compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3]
        )
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}

fun TextView.bottomDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(
                compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], drawable
        )
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
    }
}

fun RecyclerView.verticalLinearLayoutManager(): LinearLayoutManager {
    val linearLayoutManager = LinearLayoutManager(context)
    layoutManager = linearLayoutManager
    return linearLayoutManager
}

fun RecyclerView.horizontalLinearLayoutManager(): LinearLayoutManager {
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    layoutManager = linearLayoutManager
    return linearLayoutManager
}

fun RecyclerView.verticalLinearLayoutManager(span: Int): GridLayoutManager {
    val gridLayoutManager = GridLayoutManager(context, span)
    layoutManager = gridLayoutManager
    return gridLayoutManager
}

fun RecyclerView.horizontalLinearLayoutManager(span: Int): GridLayoutManager {
    val gridLayoutManager = GridLayoutManager(context, span, GridLayoutManager.HORIZONTAL, false)
    layoutManager = gridLayoutManager
    return gridLayoutManager
}

fun isNullOrEmpty(data: List<*>?): Boolean = data == null || data.isEmpty()

fun isNotEmpty(data: List<*>?): Boolean = data != null && data.isNotEmpty()


fun <T : View> T.setTopMargin(topMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = topMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.topMargin = topMargin
        }
    }
}

fun <T : View> T.setBottomMargin(bottomMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.bottomMargin = bottomMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.bottomMargin = bottomMargin
        }
    }
}

fun <T : View> T.setLeftMargin(leftMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.leftMargin = leftMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.leftMargin = leftMargin
        }
    }
}

fun <T : View> T.setRightMargin(rightMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.rightMargin = rightMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.rightMargin = rightMargin
        }
    }
}

fun newLayoutParams(
        width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT
): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(width, height)
}

fun newMarginLayoutParams(
        width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT
): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(width, height)
}