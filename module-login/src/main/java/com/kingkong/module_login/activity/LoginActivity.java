package com.kingkong.module_login.activity;


import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dxmovie.dxbase.activity.BaseActivity;
import com.dxmovie.dxbase.activity.BaseViewModelActivity;
import com.dxmovie.dxbase.activity.DXBaseActivity;
import com.kingkong.common_library.router.RouterActivityPath;
import com.kingkong.module_login.BR;
import com.kingkong.module_login.R;
import com.kingkong.module_login.databinding.LoginActivityLoginBinding;
import com.kingkong.module_login.viewmodel.LoginModel;

@Route(path = RouterActivityPath.Login.PAGER_LOGIN)
public class LoginActivity extends DXBaseActivity<LoginActivityLoginBinding, LoginModel> {
    @Override
    public int getContentViewLayout(Bundle savedInstanceState) {
        return R.layout.login_activity_login;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public LoginModel getViewModel() {
        return createViewModel(LoginModel.class);
    }
}