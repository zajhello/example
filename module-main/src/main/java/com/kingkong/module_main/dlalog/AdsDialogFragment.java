package com.kingkong.module_main.dlalog;


import androidx.lifecycle.Observer;

import com.dxmovie.dxbase.dialog.DXDialogFragment;
import com.kingkong.module_main.BR;
import com.kingkong.module_main.R;
import com.kingkong.module_main.databinding.MainDialogAdsBinding;
import com.kingkong.module_main.viewmodel.AdsModel;

import org.jetbrains.annotations.NotNull;

public class AdsDialogFragment extends DXDialogFragment<MainDialogAdsBinding> {


    @Override
    public int contentViewId() {
        return R.layout.main_dialog_ads;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @NotNull
    @Override
    public AdsModel getBindViewModel() {
        return createViewModel(AdsModel.class);
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }


    @Override
    public void initView() {
        getBindViewModel().getStatusLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) dismiss();
            }
        });
    }
}
