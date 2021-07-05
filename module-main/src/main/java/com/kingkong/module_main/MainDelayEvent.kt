package com.kingkong.module_main

import com.kingkong.common_library.interceptors.TokenInterceptor

class MainDelayEvent {

    companion object {

        @JvmStatic
        fun delayEvent() {
            TokenInterceptor.setLoginAtomic(false)
        }
    }

}