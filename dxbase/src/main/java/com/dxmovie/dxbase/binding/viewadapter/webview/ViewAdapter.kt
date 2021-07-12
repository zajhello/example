package com.dxmovie.dxbase.binding.viewadapter.webview

import android.text.TextUtils
import android.webkit.WebView
import androidx.databinding.BindingAdapter

class ViewAdapter {
    companion object {
        @BindingAdapter("render")
        fun loadHtml(webView: WebView, html: String?) {
            if (!TextUtils.isEmpty(html)) {
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        }
    }
}