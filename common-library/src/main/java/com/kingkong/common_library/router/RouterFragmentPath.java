package com.kingkong.common_library.router;

/**
 * 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * Created by goldze on 2018/6/21
 */

public class RouterFragmentPath {
    /**
     * 首页组件
     */
    public static class Home {
        private static final String HOME = "/home";
        /*首页*/
        public static final String PAGER_HOME = HOME + "/Home";
    }

    /**
     * 工作组件
     */
    public static class Work {
        private static final String TASK = "/task";
        /*工作*/
        public static final String PAGER_TASK = TASK + "/Task";
    }

    /**
     * 用户组件
     */
    public static class User {
        private static final String MINE = "/mine";
        /*我的*/
        public static final String PAGER_ME = MINE + "/Mine";
    }
}
