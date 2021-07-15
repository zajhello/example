package com.kingkong.common_library.router

class RouterFragmentPath {

    /**
     * 首页组件
     */
    object Home {
        private const val HOME = "/home"

        /*首页*/
        const val PAGER_HOME = HOME + "/Home"
    }

    /**
     * 工作组件
     */
    object Work {
        private const val TASK = "/task"

        /*工作*/
        const val PAGER_TASK = TASK + "/Task"
    }

    /**
     * 用户组件
     */
    object User {
        private const val MINE = "/mine"

        /*我的*/
        const val PAGER_ME = MINE + "/Mine"
    }
}