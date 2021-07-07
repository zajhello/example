package com.kingkong.dxmovie1;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dxmovie.dxbase.core.BaseConfig;
import com.dxmovie.dxbase.utils.TimeUtil;
import com.kingkong.common_library.helper.AppHelper;
import com.kingkong.common_library.http.DXHttpProvider;
import com.kingkong.common_library.utils.EncryUtil;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * @desc 进入app需要初始化的地方，不包括module的初始化
 */
public class GlobInitManager {

    public void init(Application application) {

        //设置ML默认网络配置
        DXHttpProvider.init();
        //初始化阿里路由框架
        if (BaseConfig.isDEV()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);

        TimeUtil.init();
        AppHelper.init();
        EncryUtil.init();

        initRxjava();
        // 自定义crash 提示
        CrashHandler.getInstance().init(application, BaseConfig.isDEV());
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void initRxjava() {
        RxJavaPlugins.setErrorHandler(throwable -> throwable.printStackTrace());
    }
}
