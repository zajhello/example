package com.kingkong.module_main.adapter;

import com.kingkong.common_library.adapter.MultiTypeSupport;
import com.kingkong.module_main.R;

import static com.kingkong.module_main.adapter.HomeItemType.ADS;
import static com.kingkong.module_main.adapter.HomeItemType.BANNER;
import static com.kingkong.module_main.adapter.HomeItemType.HORIZONTAL;
import static com.kingkong.module_main.adapter.HomeItemType.MOVIE;

public class HomeMultiTypeSupport implements MultiTypeSupport<HomeItemBean> {

    @Override
    public int getLayoutId(HomeItemBean item, int position) {
        if (item.getType() == BANNER.value) {
            return R.layout.main_item_banner;
        } else if (item.getType() == ADS.value) {
            return R.layout.main_item_ads;
        } else if (item.getType() == HORIZONTAL.value) {
            return R.layout.main_item_horizontal;
        } else if (item.getType() == MOVIE.value) {
            return R.layout.main_item_subject_001;
        } else {
            return 0;
        }
    }
}