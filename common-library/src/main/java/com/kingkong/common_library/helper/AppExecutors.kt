package com.kingkong.common_library.helper

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * app线程池操作类
 */
class AppExecutors(private val mainExecutor: Executor,
                   private val networkExecutor: Executor,
                   private val singleExecutor: Executor) {

    companion object {
        private val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppExecutors()
        }

        fun get() = instance

        /**
         * 是否在主线程
         */
        fun isMainThread(): Boolean {
            return Looper.getMainLooper().thread == Thread.currentThread()
        }

        fun mainThread() = instance.mainExecutor

        fun networkThread() = instance.networkExecutor

        fun singleThread() = instance.singleExecutor

        /**
         * 提交任务在主线程中执行
         */
        @JvmOverloads
        fun runOnMainThread(runnable: Runnable, delay:Long = 0) {
            if(isMainThread()) {
                runnable.run()
            }else {
                if(delay == 0L) {
                    instance.mainExecutor.execute(runnable)
                    return
                }
                (instance.mainExecutor as MainThreadExecutor).execute(runnable, delay)
            }
        }

        /**
         * 提交任务在IO线程中执行
         */
        fun runOnIOThread(runnable: Runnable) {
            if(isMainThread()) {
                instance.networkExecutor.execute(runnable)
            }else {
                runnable.run()
            }
        }

        /**
         * 提交任务在单线程中执行
         */
        fun runOnSingleThread(runnable: Runnable) {
            if(isMainThread()) {
                instance.singleExecutor.execute(runnable)
            }else {
                runnable.run()
            }
        }

    }

    constructor() : this(MainThreadExecutor(),
            Executors.newFixedThreadPool(5),
            Executors.newSingleThreadExecutor())

    /**
     * 主线程任务执行器
     */
    private class MainThreadExecutor : Executor {

        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }

        fun execute(command: Runnable, delay: Long) {
            mainThreadHandler.postDelayed(command, delay)
        }

    }
}