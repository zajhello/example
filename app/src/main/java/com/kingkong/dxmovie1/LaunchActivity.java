package com.kingkong.dxmovie1;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dxmovie.dxbase.activity.BaseActivity;
import com.kingkong.common_library.router.RouterActivityPath;

import java.text.DecimalFormat;

public class LaunchActivity extends BaseActivity {

    public String getLikeNum(int likeNum){
        if (likeNum >= 10000){
            DecimalFormat df = new DecimalFormat("#.#万");
            return df.format(likeNum);
        }
        return String.valueOf(likeNum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String content = getLikeNum(10000000);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.app_activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN).navigation();
                ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation();
                finish();
            }
        }, 500);
    }
}