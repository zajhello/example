package com.kingkong.common_library.constants;


public class Constants {

    public static class HomeNav {
        /**
         * 首页
         */
        public static final int NAV_ONE = 0;
        /**
         * 任务
         */
        public static final int NAV_TWO = 1;
        /**
         * 我的
         */
        public static final int NAV_THREE = 2;

    }


    public static class SPKEY {
        /**
         * 解密KEY
         */
        public static String KEY_ENCRYPT = "encryptKey";
    }

    public static class ADS {
        /****
         * 这里所以广告对应的类型
         */
        public static final String OPEN_SCREEN_AD_TYPE_AD01 = "AD01";           // 开屏广告  AD01广告类型
        public static final String WHEEL_PLATING_AD_TYPE_AD02 = "AD02";        // AD02 首页各版块轮播广告  AD02广告类型
        public static final String HOME_BOTTOM_AD_TYPE_AD03 = "AD03";           // AD03首页各频道底部广告 电影列表里面的广告 AD03广告类型
        public static final String HOME_FLOAT_AD_TYPE_AD04 = "AD04";            // AD04首页悬浮广告  AD04广告类型
        public static final String MOVIE_DETAIL_AD_TYPE_AD05 = "AD05";          // 获取视频详情广告  AD05广告类型
        public static final String MOVIE_DETAIL_PLAY_AD_TYPE_AD06 = "AD06";     // 获取视频开始广告  AD06广告类型
        public static final String MOVIE_DETAIL_END_AD_TYPE_AD07 = "AD07";      // 首页获取视频结束广告  AD07广告类型
        public static final String MOVIE_DETAIL_STOP_AD_TYPE_AD08 = "AD08";     // 获取视频暂停广告  AD08广告类型
    }
}
