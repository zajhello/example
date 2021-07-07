package com.kingkong.module_login.viewmodel;

import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dxmovie.dxbase.binding.command.BindingCommand;
import com.dxmovie.dxbase.request.BaseRequest;
import com.dxmovie.dxbase.utils.RxUtils;
import com.dxmovie.dxbase.utils.ToastUtils;
import com.dxmovie.dxbase.viewmodel.BaseViewModel;
import com.kingkong.common_library.repository.PrecondRepository;
import com.kingkong.common_library.request.SecretKeyRequest;
import com.kingkong.common_library.router.RouterActivityPath;
import com.kingkong.common_library.utils.EncryUtil;

public class LoginModel extends BaseViewModel {


    @Override
    public void onStart() {
        super.onStart();

    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(() -> {

        PrecondRepository.getInstance()
                .getSecretKetWithCache(new BaseRequest<SecretKeyRequest>(new SecretKeyRequest()))
                .doOnSubscribe(disposable -> getUi().showLoadingDialog())
                .doFinally(() -> getUi().hideLoadingDialog())
                .as(RxUtils.bindLifecycle(this))
                .subscribe(baseResponse -> {
                    String secret = baseResponse.getDatas().getSecret();
                    EncryUtil.syncDecryptKey(secret);
//                    ToastUtils.showShort(secret);
                    getUi().showToast(secret);
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN).navigation();
                }, throwable -> {
//                    ToastUtils.showShort(throwable.getMessage());
                    getUi().showToast(throwable.getMessage());
                });

        ToastUtils.showShort("防抖click写法");
      //  ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN).navigation();
        return null;
    });

    public void click(View view) {
        ToastUtils.showShort("不防抖click写法");
//        PrecondRepository.getInstance()
//                .getSecretKet(new BaseRequest<SecretKeyRequest>(new SecretKeyRequest()))
//                .doOnSubscribe(disposable -> getUi().showLoadingDialog())
//                .doFinally(() -> getUi().hideLoadingDialog())
//                .as(RxUtils.bindLifecycle(this))
//                .subscribe(baseResponse -> {
//                    String secret = baseResponse.getDatas().getSecret();
//                    EncryUtil.syncDecryptKey(secret);
//                    ToastUtils.showShort(secret);
//                }, throwable -> {
//                    ToastUtils.showShort(throwable.getMessage());
//                });
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_DEMO).navigation();
    }
}
