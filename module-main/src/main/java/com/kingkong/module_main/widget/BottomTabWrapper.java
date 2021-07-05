package com.kingkong.module_main.widget;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.dxmovie.dxbase.utils.PixelUtil;
import com.google.android.material.tabs.TabLayout;
import com.kingkong.module_main.R;
import com.kingkong.module_main.activity.MainActivity;

import java.lang.ref.WeakReference;

public class BottomTabWrapper implements TabLayout.OnTabSelectedListener {

    private WeakReference<Context> context;
    private TabLayout tabLayout;
    private TabLayout.OnTabSelectedListener tabSelectedListener;

    /**
     * 记录上一次切换的tab位置
     */
    private int lastPosition;


    public BottomTabWrapper(TabLayout tabLayout, MainActivity mainActivity, int defaultPostion) {
        this.context = new WeakReference<>(mainActivity);
        this.tabLayout = tabLayout;
        lastPosition = defaultPostion;
        for (int i = 0, count = BottomNav.values().length; i < count; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(getCustomViewLayout());
            initTabCustomView(tab, i);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.getTabAt(lastPosition).select();
    }

    private void initTabCustomView(TabLayout.Tab tab, int position) {
        View tabView = tab.getCustomView();
        TextView title = tabView.findViewById(R.id.smallLabel);
        title.setText(BottomNav.values()[position].title);
        title.setSelected(tab.isSelected());

        ImageView icon = tabView.findViewById(R.id.icon);
        icon.setImageResource(BottomNav.values()[position].src);

        TextView tag = tabView.findViewById(R.id.tag);
        if (position == 1)
            tag.setVisibility(View.VISIBLE);
    }

    /**
     * 设置切换tab
     */
    void setSelect(int position) {
        if (position == -1) {
            return;
        }

        tabLayout.getTabAt(position).select();
    }

    /**
     * 获取当前选中tab位置
     *
     * @return
     */
    public int getCurrentTabPosition() {
        return tabLayout.getSelectedTabPosition();
    }

    public TabLayout.Tab getTabAt(int position) {
        return tabLayout.getTabAt(position);
    }

    /**
     * 设置tab栏选中监听事件
     *
     * @param tabSelectedListener
     */
    public void setOnTabSelectedListener(TabLayout.OnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    @LayoutRes
    private int getCustomViewLayout() {
        return R.layout.main_bottomnavigation_item;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabUnselected(tab);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabReselected(tab);
        }
    }
}
