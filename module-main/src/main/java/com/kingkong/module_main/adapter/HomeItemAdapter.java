package com.kingkong.module_main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dxmovie.dxbase.base.AppManager;
import com.dxmovie.dxbase.utils.ToastUtils;
import com.kingkong.common_library.adapter.CommonRecyclerAdapter;
import com.kingkong.common_library.adapter.ViewHolder;
import com.kingkong.module_main.R;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.kingkong.common_library.utils.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kingkong.module_main.adapter.HomeItemType.BANNER;


public class HomeItemAdapter extends CommonRecyclerAdapter<HomeItemBean> {

    private Context mCtx;

    private HomeMultiTypeSupport typeSupport = new HomeMultiTypeSupport();

    public HomeItemAdapter(Context context) {
        super(context);
        this.mCtx = context;
        setMultiTypeSupport(typeSupport);
    }

    public HomeItemAdapter() {
        this(AppManager.currentActivity());
    }

    @Override
    public void convert(ViewHolder holder, int position, HomeItemBean item) {
        if (item.getType() == BANNER.value) {
            XBanner mXBanner = holder.getView(R.id.advertisementXB);
            initBanner(mXBanner);

        }
    }

    private void initBanner(XBanner mXBanner) {

        List<String> params = new ArrayList<>();
        params.add("1111111");
        params.add("2222222");
        params.add("3333333");

        mXBanner.setPageTransformer(Transformer.Default);
        mXBanner.setData(params, params);
        mXBanner.setAutoPlayAble(true);
        mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                ToastUtils.showShort("banner");
            }
        });

        mXBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                ImageView imageView = (ImageView) view;
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtils.loadImage(mCtx, "", R.drawable.m_home_default_bg, imageView);
            }
        });
    }
}
