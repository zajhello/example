package com.dxmovie.dxbase.base

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import java.util.*

class AppManager private constructor(context: Application?) {

    private val mStatusListeners: List<OnAppStatusChangedListener?> = ArrayList<OnAppStatusChangedListener>()

    init {
        if (context == null) {
            throw UnsupportedOperationException("u can't instantiate me...")
        }
        //注册生命周期
        context!!.registerActivityLifecycleCallbacks(activityLifecycle)
    }

    companion object {
        private var actAccount = 0
        private var currentActivity: Activity? = null
        private var isInit = false
        var sApplication: Application? = null
        var activityStack: Stack<Activity>? = null
        private val activityLifecycle: ActivityLifecycleImpl = ActivityLifecycleImpl()

        private var isForeground = false

        @Volatile
        private var instance: AppManager? = null

        @JvmStatic
        fun getInstance(): AppManager? =
                instance ?: synchronized(this) {
                    instance ?: AppManager(sApplication).also { instance = it }
                }

        @JvmStatic
        fun init(context: Application?): AppManager? {
            //flag 表示已经初始化
            sApplication = context
            isInit = true
            return getInstance()
        }

        @JvmStatic
        fun getsApplication(): Application? {
            return sApplication
        }

        /**
         * 添加Activity到堆栈
         */
        fun addActivity(activity: Activity?) {
            if (activityStack == null) {
                activityStack = Stack()
            }
            activityStack!!.add(activity)
        }

        /**
         * 移除指定的Activity
         */
        fun removeActivity(activity: Activity?) {
            if (activity != null) {
                activityStack!!.remove(activity)
            }
        }

        /**
         * 是否有activity
         */
        @JvmStatic
        fun isActivity(): Boolean {
            return if (activityStack != null) {
                !activityStack!!.isEmpty()
            } else false
        }

        /**
         * 获取当前Activity（堆栈中最后一个压入的）
         */
        @JvmStatic
        fun currentActivity(): Activity? { //        Activity activity = activityStack.lastElement();//这里不准确，不能这样用
//        return activity;
            return currentActivity
        }

        /**
         * 结束当前Activity（堆栈中最后一个压入的）
         */
        @JvmStatic
        fun finishActivity() {
            val activity = activityStack!!.lastElement()
            finishActivity(activity)
        }

        /**
         * 结束指定的Activity
         */
        @JvmStatic
        fun finishActivity(activity: Activity?) {
            if (activity != null) {
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }

        /**
         * 结束指定类名的Activity
         */
        @JvmStatic
        fun finishActivity(cls: Class<*>) {
            for (activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }

        /**
         * 结束所有Activity
         */
        @JvmStatic
        fun finishAllActivity() {
            var i = 0
            val size = activityStack!!.size
            while (i < size) {
                if (null != activityStack!![i]) {
                    finishActivity(activityStack!![i])
                }
                i++
            }
            activityStack!!.clear()
        }

        /**
         * 获取指定的Activity
         *
         * @author kymjs
         */
        @JvmStatic
        fun getActivity(cls: Class<*>): Activity? {
            if (activityStack != null) for (activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    return activity
                }
            }
            return null
        }

        @JvmStatic
        fun isAppForeground(): Boolean {
            return actAccount != 0
        }

        @JvmStatic
        fun getActivityLifecycle(): ActivityLifecycleImpl? {
            return AppManager.activityLifecycle
        }

        @JvmStatic
        fun getTopActivityOrApp(): Context? {
            return if (isAppForeground()) {
                val topActivity = currentActivity()
                topActivity ?: sApplication
            } else {
                sApplication
            }
        }

        /**
         * 退出应用程序
         */
        @JvmStatic
        fun AppExit() {
            try {
                finishAllActivity()
                // 杀死该应用进程
                Process.killProcess(Process.myPid())
                //            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
//            System.exit(0);
            } catch (e: Exception) {
                activityStack!!.clear()
                e.printStackTrace()
            }
        }

        /**
         * 是否在前台
         *
         * @return
         */
        @JvmStatic
        fun getIsForeground(): Boolean {
            return isForeground
        }

        //判断Activity是否Destroy
        @JvmStatic
        fun isDestroy(activity: Activity?): Boolean {
            return activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
        }
    }

    class ActivityLifecycleImpl : ActivityLifecycleCallbacks {
        private val mDestroyedListenerMap: MutableMap<Activity, MutableSet<OnActivityOnDestroyListener?>?> = HashMap()
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity
            if (actAccount == 0) {
                postStatus(activity, true)
                isForeground = true
            }
            actAccount++
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {
            actAccount--
            if (actAccount == 0) {
                postStatus(activity!!, false)
                isForeground = false
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {
            consumeOnActivityDestroyedListener(activity)
            removeActivity(activity)
        }

        /**
         * activity onDestory Listener
         *
         * @param activity
         */
        fun consumeOnActivityDestroyedListener(activity: Activity) {
            val iterator: Iterator<Map.Entry<Activity, Set<OnActivityOnDestroyListener?>?>> = mDestroyedListenerMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.key === activity) {
                    val value = entry.value
                    for (listener in value!!) {
                        listener!!.onActivityDestroyed(activity)
                    }
                }
            }
        }

        private fun postStatus(activity: Activity, isForeground: Boolean) {
            if (instance!!.mStatusListeners.isEmpty()) return
            for (statusListener in instance!!.mStatusListeners) {
                if (isForeground) {
                    statusListener!!.onForeground(activity)
                } else {
                    statusListener!!.onBackground(activity)
                }
            }
        }

        /**
         * add activity onDestory Listener
         *
         * @param activity
         * @param listener
         */
        fun addOnActivityDestroyedListener(activity: Activity?, listener: OnActivityOnDestroyListener?) {
            if (activity == null || listener == null) return
            val listeners: MutableSet<OnActivityOnDestroyListener?>?
            if (!mDestroyedListenerMap.containsKey(activity)) {
                listeners = HashSet<OnActivityOnDestroyListener?>()
                mDestroyedListenerMap[activity] = listeners
            } else {
                listeners = mDestroyedListenerMap[activity]
                if (listeners!!.contains(listener)) return
            }
            listeners.add(listener)
        }

        /**
         * 移除监听
         *
         * @param activity
         */
        fun removeOnActivityDestroyedListener(activity: Activity?) {
            if (activity == null) return
            mDestroyedListenerMap.remove(activity)
        }
    }

    fun addOnAppStatusChangedListener(listener: OnAppStatusChangedListener?) {
        mStatusListeners.toMutableList().add(listener)
    }

    fun removeOnAppStatusChangedListener(listener: OnAppStatusChangedListener?) {
        mStatusListeners.toMutableList().remove(listener)
    }

    /**
     * 清除栈里所有activity ，除了自己
     *
     * @param cls
     */
    fun cleanOther(cls: Class<*>) {
        if (activityStack != null) {
            for (activity in activityStack!!) {
                if (activity.javaClass != cls) {
                    activity.finish()
                }
            }
        }
    }

    interface OnActivityOnDestroyListener {
        fun onActivityDestroyed(activity: Activity?)
    }

    interface OnAppStatusChangedListener {
        fun onForeground(activity: Activity?)
        fun onBackground(activity: Activity?)
    }
}