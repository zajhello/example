package com.kingkong.common_library.router

class RouterActivityPath {

    /**
     * 主业务组件
     */
    object Main {
        private const val MAIN = "/main"

        /*主业务界面*/
        const val PAGER_MAIN = MAIN + "/Main"

        /*demo界面*/
        const val PAGER_DEMO = MAIN + "/Demo"
    }

    /**
     * 身份验证组件
     */
    object Login {
        private const val LOGIN = "/login"

        /*登录界面*/
        const val PAGER_LOGIN = LOGIN + "/Login"
    }
}