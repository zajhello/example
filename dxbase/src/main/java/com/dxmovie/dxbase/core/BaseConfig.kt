package com.dxmovie.dxbase.core

object BaseConfig {

    private var ENV = Environment.DEV

    @JvmStatic
    fun isDEV(): Boolean {
        return ENV == Environment.DEV
    }

    @JvmStatic
    fun isProduct(): Boolean {
        return ENV == Environment.PRODUCT
    }
}