package com.kingkong.common_library.adapter

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View?, position: Int)

    fun onItemLongClick(view: View?, position: Int): Boolean {
        return false
    }
}