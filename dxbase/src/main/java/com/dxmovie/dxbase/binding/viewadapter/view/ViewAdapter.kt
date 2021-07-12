package com.dxmovie.dxbase.binding.viewadapter.view

import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.databinding.BindingAdapter
import com.dxmovie.dxbase.binding.command.BindingCommand
import com.dxmovie.dxbase.binding.command.BindingCommandWithParams
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class ViewAdapter {

    companion object {
        //防重复点击间隔(秒)
        const val CLICK_INTERVAL = 1

        /**
         * requireAll 是意思是是否需要绑定全部参数, false为否
         * View的onClick事件绑定
         * onClickCommand 绑定的命令,
         * isThrottleFirst 是否开启防止过快点击
         */
        @JvmStatic
        @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
        fun onClickCommand(view: View?, clickCommand: BindingCommand?, isThrottleFirst: Boolean) {
            if (isThrottleFirst) {
                RxView.clicks(view!!)
                        .subscribe { clickCommand?.executeAction() }
            } else {
                RxView.clicks(view!!)
                        .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.SECONDS) //1秒钟内只允许点击1次
                        .subscribe { clickCommand?.executeAction() }
            }
        }

        /**
         * view的onLongClick事件绑定
         */
        @JvmStatic
        @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
        fun onLongClickCommand(view: View?, clickCommand: BindingCommand?) {
            RxView.longClicks(view!!)
                    .subscribe { clickCommand?.executeAction() }
        }

        /**
         * 回调控件本身
         *
         * @param currentView
         * @param bindingCommand
         */
        @JvmStatic
        @BindingAdapter(value = ["currentView"], requireAll = false)
        fun replyCurrentView(currentView: View?, bindingCommand: BindingCommandWithParams<View?>?) {
            bindingCommand?.executeAction(currentView)
        }

        /**
         * view是否需要获取焦点
         */
        @JvmStatic
        @BindingAdapter("requestFocus")
        fun requestFocusCommand(view: View, needRequestFocus: Boolean) {
            if (needRequestFocus) {
                view.isFocusableInTouchMode = true
                view.requestFocus()
            } else {
                view.clearFocus()
            }
        }

        /**
         * view的焦点发生变化的事件绑定
         */
        @JvmStatic
        @BindingAdapter("onFocusChangeCommand")
        fun onFocusChangeCommand(view: View, onFocusChangeCommand: BindingCommandWithParams<Boolean?>?) {
            view.onFocusChangeListener = OnFocusChangeListener { v, hasFocus -> onFocusChangeCommand?.executeAction(hasFocus) }
        }

        /**
         * view的显示隐藏
         */
        @JvmStatic
        @BindingAdapter(value = ["isVisible"], requireAll = false)
        fun isVisible(view: View, visibility: Boolean) {
            if (visibility) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
//    @BindingAdapter({"onTouchCommand"})
//    public static void onTouchCommand(View view, final ResponseCommand<MotionEvent, Boolean> onTouchCommand) {
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (onTouchCommand != null) {
//                    return onTouchCommand.execute(event);
//                }
//                return false;
//            }
//        });
//    }
    }
}