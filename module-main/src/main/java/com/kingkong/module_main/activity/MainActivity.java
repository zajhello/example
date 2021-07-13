package com.kingkong.module_main.activity;

import android.os.Bundle;
import android.os.PowerManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dxmovie.dxbase.activity.BaseActivity;
import com.dxmovie.dxbase.base.AppManager;
import com.dxmovie.dxbase.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.kingkong.common_library.adapter.FragmentXPagerAdapter;
import com.kingkong.common_library.router.RouterActivityPath;
import com.kingkong.module_main.MainDelayEvent;
import com.kingkong.module_main.R;
import com.kingkong.module_main.dlalog.AdsDialogFragment;
import com.kingkong.module_main.fragment.HomeFragment;
import com.kingkong.module_main.fragment.MineFragment;
import com.kingkong.module_main.fragment.TaskFragment;
import com.kingkong.module_main.widget.BottomTabWrapper;

import java.util.ArrayList;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getName();

    private TabLayout tabLayout;
    private BottomTabWrapper mNavWrapper;
    private ViewPager viewPager;
    private FragmentPagerAdapter mPageAdapter;


    private AdsDialogFragment mAdsDialog;


    private PowerManager.WakeLock mWakeLock;
    private long mExitTime = 0;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_main);
        MainDelayEvent.delayEvent();
        acquireWakeLock();
        initView();
        initAdsDialog();
    }


    private void initAdsDialog() {
        mAdsDialog = new AdsDialogFragment();
        mAdsDialog.setCanceledOnTouchOutside(false);
        mAdsDialog.setCancelable(false);
        mAdsDialog.show(getSupportFragmentManager(), "AdsDialogFragment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseWakeLock();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showShort("再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.AppExit();
        }
    }

    private void initView() {
        tabLayout = findViewById(R.id.app_bottom_navigation);
        mNavWrapper = new BottomTabWrapper(tabLayout, this, position);
        mNavWrapper.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ArrayList<Fragment> fragments = new java.util.ArrayList<Fragment>() {
            {
                add(HomeFragment.newInstance());
                add(TaskFragment.newInstance());
                add(MineFragment.newInstance());
            }
        };
        viewPager = findViewById(R.id.viewPager);
        mPageAdapter = new FragmentXPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mPageAdapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(position);
    }

    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                    PowerManager.ON_AFTER_RELEASE, TAG);
            mWakeLock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}