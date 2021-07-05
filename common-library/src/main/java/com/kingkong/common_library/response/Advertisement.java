package com.kingkong.common_library.response;

import android.text.TextUtils;

import java.io.Serializable;

public class Advertisement implements Serializable {

    public static final String LINKED_INVITE_MONEY = "invite_money";        // 邀请赚钱
    public static final String LINKED_WITH_DRAW = "withdraw";               // 提现
    public static final String LINKED_HOME_PAGE = "homepage";               // 主页

    public static final String DX_COUNT_TYPE = "M08";

    private static final String WEB_PAGE_WITH_TITLE_BAR = "webHasHead";//广告的h5页面是否有titleBar
    public static final String WEB_PAGE_NO_WITH_TITLE_BAR = "webNoHead";  // 广告的h5页面没有titleBar
    public static final String WEB_PAGE_OPEN_BROWSER = "openBrowser";  // 页面浏览器打开

    public Long id;//广告ID
    public Long ad_id;//广告ID // 1.1.7加

    public String ad_type; // 广告类型
    public String groupname;//广告栏目，例如轮播首页广告,电影首页轮播广告
    public String ad_image;//广告图片
    public String canshu;//广告类别，就是传递过来的adtype广告类型
    public int jump_type;//inner app内部视频地址 outer 内部浏览器打开外部地址 outerAdv 外部浏览器打开外部地址
    public String jumpType;
    public int shownum;//展示次数；为0时代表无限制
    public String ad_url;//广告地址
    public String desc;

    public String other;
    public int weigh;//排序字段 一般是int 产品说这个字段必须配置

    public String getJumpType() {
        if (jump_type == 1) {
            return "outer";
        }
        if (jump_type == 2) {
            return "outerAdv";
        }
        if (jump_type == 3) {
            return "inner";
        }
        return "";
    }

    //广告是系统浏览器打开
    public boolean isAdsSystemBrowserOpen() {
        return jump_type == 2;
    }

    //广告是app webView打开
    public boolean isAdsAppWebViewOpen() {
        return jump_type == 2;
    }

    //广告是否跳转到内部功能模块
    public boolean isAdsToAppFunction() {
        return jump_type == 2;
    }

    /***
     * 判断当前广告是否有效
     * 根据广告的展示次数来计算
     * @return
     */
    public boolean isAdvertisementInvitation(int count) {
        if (shownum == 0 || count <= shownum) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Advertisement) obj).ad_image.equals(ad_image);
    }

    /**
     * 有些情况下 例如
     * 搜索结果中插入的广告 会使用@连接俩个字符串 @前段是列表上边字段 后边是列表中下边字段
     *
     * @return
     */
    public String[] splitAdsDesc() {
        try {
            String[] result = desc.split("@");
            if (result != null && result.length > 1) {
                return result;
            }
            return null;
        } catch (Exception e) {

        }
        return null;
    }

    public boolean isAdsWebPageHasTitleBar() {
        return !TextUtils.isEmpty(ad_url) && ad_url.contains(WEB_PAGE_WITH_TITLE_BAR);
    }

    public static boolean isAdsWebPageHasTitleBar(String url) {
        return !TextUtils.isEmpty(url) && url.contains(WEB_PAGE_WITH_TITLE_BAR);
    }

    public static boolean isAdsBrowserOpen(String url) {
        return !TextUtils.isEmpty(url) && url.contains(WEB_PAGE_OPEN_BROWSER);
    }

    /**
     * 广告插入的位置 产品定义的位置和我们程序数组下标位置不一样
     *
     * @return
     */
    public int getAdsInsertPosition() {
        int insertPosition = weigh - 1;
        return insertPosition >= 0 ? insertPosition : 0;
    }

    public Advertisement copyData() {
        Advertisement copy = new Advertisement();
        copy.desc = desc;
        copy.ad_id = ad_id;
        copy.ad_image = ad_image;
        copy.ad_url = ad_url;
        copy.jump_type = jump_type;
        copy.jumpType = jumpType;
        copy.shownum = shownum;
        copy.canshu = canshu;
        copy.other = other;
        copy.weigh = weigh;
        copy.id = id;
        copy.groupname = groupname;
        return copy;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", ad_id=" + ad_id +
                ", groupname='" + groupname + '\'' +
                ", ad_image='" + ad_image + '\'' +
                ", canshu='" + canshu + '\'' +
                ", jump_type=" + jump_type +
                ", jumpType='" + jumpType + '\'' +
                ", shownum=" + shownum +
                ", ad_url='" + ad_url + '\'' +
                ", desc='" + desc + '\'' +
                ", other='" + other + '\'' +
                ", weigh=" + weigh +
                '}';
    }
}

