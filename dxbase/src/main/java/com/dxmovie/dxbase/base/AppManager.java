package com.dxmovie.dxbase.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * activity堆栈式管理以及其他的一些处理
 */
public class AppManager {
    private static int actAccount = 0;
    private static Activity currentActivity = null;
    private static boolean isInit = false;
    private static Application sApplication;
    private static Stack<Activity> activityStack;
    private static AppManager instance;
    private static ActivityLifecycleImpl activityLifecycle = new ActivityLifecycleImpl();
    private final List<OnAppStatusChangedListener> mStatusListeners = new ArrayList<>();

    //
    private static boolean isForeground = false;

    private AppManager(Application context) {
        if (context == null) {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }
        sApplication = context;
        //注册生命周期
        sApplication.registerActivityLifecycleCallbacks(activityLifecycle);
    }

    /**
     * 单例模式 在application里面初始化
     *
     * @return AppManager
     */
    public static AppManager init(Application context) {
        if (instance == null) {
            instance = new AppManager(context);
        }
        //flag 表示已经初始化
        isInit = true;
        return instance;
    }

    public static Application getsApplication() {
        if (sApplication == null) {
            throw new IllegalArgumentException("Application is null");
        }
        return sApplication;
    }

    public static AppManager getInstance() {
        if (!isInit) {
            throw new IllegalArgumentException("AppManage class not init");
        }
        return instance;
    }

    private static Stack<Activity> getActivityStack() {
        return activityStack;
    }


    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public static void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }


    /**
     * 是否有activity
     */
    public static boolean isActivity() {
        if (activityStack != null) {
            return !activityStack.isEmpty();
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
//        Activity activity = activityStack.lastElement();//这里不准确，不能这样用
//        return activity;
        return currentActivity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 清除栈里所有activity ，除了自己
     *
     * @param cls
     */
    public void cleanOther(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
        }
    }

    public static boolean isAppForeground() {
        return actAccount != 0;
    }

    public static ActivityLifecycleImpl getActivityLifecycle() {
        return activityLifecycle;
    }

    public static Context getTopActivityOrApp() {
        if (isAppForeground()) {
            Activity topActivity = currentActivity();
            return topActivity == null ? getsApplication() : topActivity;
        } else {
            return getsApplication();
        }
    }

    public void addOnAppStatusChangedListener(OnAppStatusChangedListener listener) {
        mStatusListeners.add(listener);
    }

    public void removeOnAppStatusChangedListener(OnAppStatusChangedListener listener) {
        mStatusListeners.remove(listener);
    }

    public static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {
        final Map<Activity, Set<OnActivityOnDestroyListener>> mDestroyedListenerMap = new HashMap<>();

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            currentActivity = activity;
            if (AppManager.actAccount == 0) {
                postStatus(activity,true);
                isForeground = true;
            }
            AppManager.actAccount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            currentActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            AppManager.actAccount--;
            if (AppManager.actAccount == 0) {
                postStatus(activity, false);
                isForeground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            consumeOnActivityDestroyedListener(activity);
            AppManager.removeActivity(activity);
        }

        /**
         * activity onDestory Listener
         *
         * @param activity
         */
        public void consumeOnActivityDestroyedListener(Activity activity) {
            Iterator<Map.Entry<Activity, Set<OnActivityOnDestroyListener>>> iterator =
                    mDestroyedListenerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Activity, Set<OnActivityOnDestroyListener>> entry = iterator.next();
                if (entry.getKey() == activity) {
                    Set<OnActivityOnDestroyListener> value = entry.getValue();
                    for (OnActivityOnDestroyListener listener : value) {
                        listener.onActivityDestroyed(activity);
                    }
                }
            }
        }

        private void postStatus(Activity activity, final boolean isForeground) {
            if (instance.mStatusListeners.isEmpty()) return;
            for (OnAppStatusChangedListener statusListener : instance.mStatusListeners) {
                if (isForeground) {
                    statusListener.onForeground(activity);
                } else {
                    statusListener.onBackground(activity);
                }
            }
        }

        /**
         * add activity onDestory Listener
         *
         * @param activity
         * @param listener
         */
        public void addOnActivityDestroyedListener(final Activity activity, final OnActivityOnDestroyListener listener) {
            if (activity == null || listener == null) return;
            Set<OnActivityOnDestroyListener> listeners;
            if (!mDestroyedListenerMap.containsKey(activity)) {
                listeners = new HashSet();
                mDestroyedListenerMap.put(activity, listeners);
            } else {
                listeners = mDestroyedListenerMap.get(activity);
                if (listeners.contains(listener)) return;
            }
            listeners.add(listener);
        }

        /**
         * 移除监听
         *
         * @param activity
         */
        public void removeOnActivityDestroyedListener(final Activity activity) {
            if (activity == null) return;
            mDestroyedListenerMap.remove(activity);
        }
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
//            System.exit(0);
        } catch (Exception e) {
            activityStack.clear();
            e.printStackTrace();
        }
    }

    /**
     * 是否在前台
     *
     * @return
     */
    public static boolean getIsForeground() {
        return isForeground;
    }

    //判断Activity是否Destroy
    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnActivityOnDestroyListener {
        void onActivityDestroyed(Activity activity);
    }

    public interface OnAppStatusChangedListener {

        void onForeground(Activity activity);

        void onBackground(Activity activity);
    }
}