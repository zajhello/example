package com.dxmovie.dxbase.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dxmovie.dxbase.R;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * GitHub: https://github.com/laobie
 */
public class StatusBarUtil {

    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;

    /**
     * 状态栏显示和隐藏
     *     该方法会让状态栏彻底消失，不占用屏幕空间
     */
    public static void visiable(Activity activity, boolean visiable) {
        if (visiable) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /*
        尽量在设置玩布局文件后再进行相关设置
            以下步骤可以根据需要省略
            通常的设置顺序：设置全屏（搭配使用白天、夜间模式） --> 改变颜色（搭配使用白天、夜间模式）
     */

    /**
     * 透明状态栏（安卓4.4支持）
     *      4.4 -- 背景透明    6.0及以后 -- 背景全透明
     *      触发全屏
     */
    public static void translucent(Activity activity) {
        // 该方案支持安卓5.0全透明，但是由于安卓5.0无法修改通知栏图表字体颜色，为了更好的兼容性，将要求放宽到6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //去除效果不佳的半透明状态栏(如果设置了的话)
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //这里的原理是直接设置了完全透明的状态栏，并且保留了内容延伸的效果
            //全屏显示设置新的状态栏:延伸内容到状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        // 安卓4.4以上
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置为白天模式：黑色图标（安卓6.0支持）
     *      仅替换通知栏图标，通知来保持原来颜色（默认黑色）
     *      不触发全屏，也不会清除全屏属性
     */
    public static void lightMode(Activity activity) {
        setMIUIStatusBarDarkIcon(activity, true);
        setMeizuStatusBarDarkIcon(activity, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置为夜间模式：白色图标（安卓6.0支持）
     *      仅替换通知栏图标，通知来保持原来颜色（默认黑色）
     *      不触发全屏，也不会清除全屏属性
     */
    public static void darkMode(Activity activity) {
        setMIUIStatusBarDarkIcon(activity, false);
        setMeizuStatusBarDarkIcon(activity, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 修改通知栏颜色（安卓6.0支持）
     *      不触发全屏，并且会清除全屏属性
     */
    public static void changeColor(Activity activity, int color) {
        // 只有6.0及以上支持修改颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }else {
            try {
                setYtfStatusBar(activity,color,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 根据状态栏的高度自动位移目标View
     *      只会增加paddingTop，其它方向的保持不变
     */
    public static void offsetForStatusBar(Context context, View needOffsetView) {
        if (needOffsetView == null){
            return;
        }
        Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
        if (haveSetOffset == null || !(Boolean) haveSetOffset) {
            needOffsetView.setPadding(needOffsetView.getPaddingLeft(), needOffsetView.getPaddingTop() + getStatusBarHeight(context),
                    needOffsetView.getPaddingRight(), needOffsetView.getPaddingBottom());
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 这一套基本好使
     * @param activity
     * @param visible
     */
    public static void setNavigationBarVisible(Activity activity, boolean visible) {
        if (visible){
            showNavigationBar(activity);
        } else {
            hideNavigationBar(activity);
        }
    }

    private static void hideNavigationBar(Activity activity){
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private static void showNavigationBar(Activity activity){
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    /***
     * 隐藏导航栏
     * @param activity
     */
    public static void hideStatusNavigationBar(Activity activity){
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /***
     * 设置底部导航栏透明
     * @param activity
     */
    public static void setNavigationBarTranslucent(Activity activity, boolean landscape){
        // 这里是控制导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity!=null) {
            Window window = activity.getWindow();
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(landscape){
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 隐藏虚拟栏 ，显示的时候再隐藏掉
     * @param window
     */
    static public void hideNavigationBar(final Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                window.getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }

    /**
     * dialog 需要全屏的时候用，和clearFocusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     * @param window
     */
    static public void focusNotAle(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * dialog 需要全屏的时候用，focusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     * @param window
     */
    static public void clearFocusNotAle(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 支持方法
    ///////////////////////////////////////////////////////////////////////////



    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    private static void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) { }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private static void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) { }
    }


    ////////////////////////////////////////以下是修改背景颜色和状态栏的字体颜色///////////////////////////////////////////////////////
    /**
     * @param activity 上下文
     * @param statusBg 状态栏颜色
     * @param darkMode true：字体为黑  false：字体为白
     */
    public static void setYtfStatusBar(Activity activity, int statusBg, boolean darkMode) {
        final int gray = ContextCompat.getColor(activity, R.color.grey_e5);
        /**
         * style的windowTranslucentNavigation设置为false后，状态栏无法达到沉浸效果
         * 设置UI FLAG 让布局能占据状态栏的空间，达到沉浸效果
         */
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(option);

        if (isMiUIV7OrAbove()) {
            //设置状态栏文字黑色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
                if (darkMode) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                activity.getWindow().getDecorView().setSystemUiVisibility(vis);
                //设置状态栏颜色
                setStatusBarBackground_V21(activity, statusBg);
            } else {
                //浅背景黑字
                MIUISetStatusBarLightMode(activity, darkMode);
                setStatusBarBackground_V19(activity, statusBg);
            }

            return;
        } else if (isMiUIV6OrAbove()) {
            //白背景黑字
            MIUISetStatusBarLightMode(activity, darkMode);
            setStatusBarBackground_V19(activity, statusBg);
            return;
        }

        if (isFlymeV4OrAbove()) {
            FlymeSetStatusBarLightMode(activity, darkMode);
            setStatusBarBackground_V19(activity, statusBg);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //api23+ 6.0以上的
            //设置状态栏文字黑色
            int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (darkMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(vis);
            //设置状态栏颜色
            setStatusBarBackground_V21(activity, statusBg);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //api21-23   5.0~6.0
            //字体不能变色，所以statusBar颜色改成灰的
            if (darkMode) {
                setStatusBarBackground_V21(activity, gray);
            } else {
                setStatusBarBackground_V21(activity, statusBg);
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //api19-21
            if (darkMode) {
                setStatusBarBackground_V19(activity, gray);
            } else {
                setStatusBarBackground_V19(activity, statusBg);
            }
        }

    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void setStatusBarBackground_V21(Activity activity, int color) {
        Window window = activity.getWindow();
        //首先清除默认的FLAG_TRANSLUCENT_STATUS
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void setStatusBarBackground_V19(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);

        decorViewGroup.addView(statusBarView);

        //设置标题栏下移
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    private static boolean isMiUIV6OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 4;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}