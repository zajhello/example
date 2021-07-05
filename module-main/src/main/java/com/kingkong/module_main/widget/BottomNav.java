package com.kingkong.module_main.widget;

import android.os.Bundle;

import com.kingkong.common_library.constants.Constants;
import com.kingkong.module_main.R;
import com.kingkong.module_main.fragment.HomeFragment;
import com.kingkong.module_main.fragment.MineFragment;
import com.kingkong.module_main.fragment.TaskFragment;

public enum BottomNav {

    TAB_ONE(Constants.HomeNav.NAV_ONE, "首页", R.drawable.selector_shouye_shouye, HomeFragment.class, null),
    TAB_TWO(Constants.HomeNav.NAV_TWO, "任务", R.drawable.selector_shouye_renwu, TaskFragment.class, null),
    TAB_THREE(Constants.HomeNav.NAV_THREE, "我的", R.drawable.selector_shouye_gerenzhongxin, MineFragment.class, null);

    public int index;
    public String title;
    public int src;
    public Class<?> clz;
    public Bundle args;

    BottomNav(int index, String tittle, int src, Class<?> clz, Bundle args) {
        this.index = index;
        this.title = tittle;
        this.src = src;
        this.clz = clz;
        this.args = args;
    }

    public static void setArgs(int index, Bundle args) {
        for (BottomNav nav : BottomNav.values()) {
            if (nav.index == index) {
                nav.args = args;
                break;
            }
        }
    }

}
