package com.kingkong.module_main.dlalog;

import android.app.Activity;

import com.dxmovie.dxbase.dialog.BaseDialog;
import com.kingkong.module_main.R;

public class AdsDialog extends BaseDialog {

    private static final String TAG = AdsDialog.class.getName();

    public AdsDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected String getDialogName() {
        return TAG;
    }

    @Override
    public void initView() {

    }

    @Override
    public int getCustomLayout() {
        return R.layout.main_ads_dialog;
    }

    @Override
    public boolean isCancel() {
        return true;
    }

    @Override
    public boolean isCancelOutside() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

}
