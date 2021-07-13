package com.kingkong.common_library.adapter

interface MultiTypeSupport<T> {
    fun getLayoutId(item: T, position: Int): Int
}