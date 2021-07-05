package com.kingkong.common_library.source;

public class Api {

    public static final String searchResultSecret = "/app/lookup/searchResultSecret";

    public static final String getSecret = "/user/v1/getSecret";
    public static final String searchAppSwitchConfig = "/lookup/searchAppSwitchConfig";
    public static final String searchUserCenterUrlConfig = "/lookup/searchUserCenterUrlConfig";
    public static final String upgrade = "/edition/v1/upgrade";
    public static final String category = "/movie/v1/category";
    public static final String advList = "/subject/v1/advList";
    public static final String movieInfoList = "/subject/v1/movieInfoList";
    public static final String advInfo = "/movie/adv/advInfo";
    public static final String adv1 = "/php/api/ad/v1";




    /**
     * 是否需要加密
     */
    public static boolean need2Encry(String urlPart) {
        if (!urlPart.equals(getSecret)
                && !urlPart.equals(searchAppSwitchConfig)
                && !urlPart.equals(searchUserCenterUrlConfig)
                && !urlPart.equals(upgrade)
                && !urlPart.equals(category)
                && !urlPart.equals(advList)
                && !urlPart.equals(movieInfoList)
                && !urlPart.equals(advInfo)
                && !urlPart.equals(adv1)
        ) {
            return true;
        } else {
            return false;
        }
    }
}
