/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kingkong.module_main.autoSize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingkong.common_library.router.RouterActivityPath;
import com.kingkong.module_main.R;
import com.kingkong.module_main.dlalog.AdsDialog;
import com.kingkong.module_main.dlalog.AdsDialogFragment;

/**
 *
 */
//实现 CancelAdapt 即可取消当前 Activity 的屏幕适配, 并且这个 Activity 下的所有 Fragment 和 View 都会被取消适配
//public class MainActivity extends AppCompatActivity implements CancelAdapt {
@Route(path = RouterActivityPath.Main.PAGER_DEMO)
public class DemoActivity extends AppCompatActivity {

    AdsDialog mAds;
    AdsDialogFragment mAdsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_demo);
    }

    /**
     * 需要注意的是暂停 AndroidAutoSize 后, AndroidAutoSize 只是停止了对后续还没有启动的 {@link Activity} 进行适配的工作
     * 但对已经启动且已经适配的 {@link Activity} 不会有任何影响
     *
     * @param view {@link View}
     */
    public void stop(View view) {

//        Toast.makeText(getApplicationContext(), "AndroidAutoSize stops working!", Toast.LENGTH_SHORT).show();
//        AutoSizeConfig.getInstance().stop(this);
        mAds = new AdsDialog(this);
        mAds.show();
    }

    /**
     * 需要注意的是重新启动 AndroidAutoSize 后, AndroidAutoSize 只是重新开始了对后续还没有启动的 {@link Activity} 进行适配的工作
     * 但对已经启动且在 stop 期间未适配的 {@link Activity} 不会有任何影响
     *
     * @param view {@link View}
     */
    public void restart(View view) {
//        Toast.makeText(getApplicationContext(), "AndroidAutoSize continues to work", Toast.LENGTH_SHORT).show();
//        AutoSizeConfig.getInstance().restart();
        mAdsDialog = new AdsDialogFragment();
        mAdsDialog.setCanceledOnTouchOutside(false);
        mAdsDialog.setCancelable(false);
        mAdsDialog.show(getSupportFragmentManager(), "AdsDialogFragment");
    }

    /**
     * 跳转到 {@link CustomAdaptActivity}, 展示项目内部的 {@link Activity} 自定义适配参数的用法
     *
     * @param view {@link View}
     */
    public void goCustomAdaptActivity(View view) {
        startActivity(new Intent(getApplicationContext(), CustomAdaptActivity.class));
    }
}
