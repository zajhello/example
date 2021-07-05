package com.dxmovie.dxbase.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtil {


    /**
     * 时间检测 key
     */
    private static String URL_CHECK_1H = "urlCheck1H";
    /**
     * 时间检测 key
     */
    private static String URL_CHECK_10M = "urlCheck10M";

    /**
     * 时间检测 key
     */
    private static String URL_CHECK_30M = "urlCheck30M";

    public static boolean canRetry() {

        long lasttime = SPUtils.getInstance().getLong(URL_CHECK_10M);
        long curTime = System.currentTimeMillis();
        if (curTime - lasttime > 10 * 1000 * 60) {
            SPUtils.getInstance().put(URL_CHECK_10M, curTime);
            return true;
        }
        return false;
    }

    public static boolean canRetryAfter30M() {
        long lasttime = SPUtils.getInstance().getLong(URL_CHECK_30M);
        long curTime = System.currentTimeMillis();
        if (lasttime <= 0) {
            SPUtils.getInstance().put(URL_CHECK_30M, curTime);
            return false;
        }
        if (curTime - lasttime > 30 * 1000 * 60) {
            SPUtils.getInstance().put(URL_CHECK_30M, curTime);
            return true;
        }
        return false;
    }

    public static boolean canRetryAfter1Hour(Context mContext) {
        long lasttime = SPUtils.getInstance().getLong(URL_CHECK_1H);
        long curTime = System.currentTimeMillis();
        if (lasttime <= 0) {
            SPUtils.getInstance().put(URL_CHECK_1H, curTime);
            return false;
        }
        if (curTime - lasttime > 60 * 1000 * 60) {
            SPUtils.getInstance().put(URL_CHECK_1H, curTime);
            return true;
        }
        return false;
    }

    public static void init() {
        long curTime = System.currentTimeMillis();
        SPUtils.getInstance().put(URL_CHECK_1H, curTime);
        SPUtils.getInstance().put(URL_CHECK_30M, curTime);
//        SPUtils.getInstance().put(URL_CHECK_10M, curTime);
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return format.format(new Date(time));
    }
}
