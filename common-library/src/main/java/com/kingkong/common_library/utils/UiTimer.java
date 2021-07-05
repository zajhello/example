package com.kingkong.common_library.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public final class UiTimer {
    private boolean isSchedule;                             // 是否已经开始了循环，这个表示循环是否终止
    private long delay;                                      // 每次执行间隔的时间 毫秒
    private TimerDriver timerDriver;                         // 定时器的内核驱动器
    private OnTimerStartListener onTimerStartListener;      // 定时器启动时的回调
    private UiTimerExecuteBody uiTimerExecuteBody;          // 定时器执行的任务
    private OnTimerFinishListener onTimerFinishListener;    // 当定时器结束时的回调

    public UiTimer() {
        this(0, null, null);
    }

    public UiTimer(long delay) {
        this(delay, null, null);
    }

    public UiTimer(long delay, UiTimerExecuteBody uiTimerExecuteBody, OnTimerFinishListener onTimerFinishListener) {
        this.setDelay(delay);
        this.timerDriver = new DefaultTimerDriver();
        this.uiTimerExecuteBody = uiTimerExecuteBody;
        this.onTimerFinishListener = onTimerFinishListener;
    }

    public synchronized UiTimer schedule() {
        if (!isSchedule) {
            isSchedule = true;
            if (onTimerStartListener != null) {
                onTimerStartListener.onTimerStart(this, timerDriver);
            }
            timerDriver.startDrive(this);
        }
        return this;
    }

    public synchronized UiTimer cancle() {
        if (isSchedule) {
            isSchedule = false;
            timerDriver.stopDrive(this);
            if (onTimerFinishListener != null) {
                onTimerFinishListener.onTimerFinish(this, timerDriver);
            }
        }
        return this;
    }

    public UiTimer setTimerDriver(TimerDriver timerDriver) {
        this.timerDriver = timerDriver;
        return this;
    }

    public synchronized boolean isSchedule() {
        return isSchedule;
    }

    public synchronized UiTimer setDelay(long delay) {
        this.delay = delay < 0 ? 0 : delay;
        return this;
    }

    public synchronized long getDelay() {
        return delay;
    }

    public synchronized UiTimer setOnTimerStartListener(OnTimerStartListener onTimerStartListener) {
        this.onTimerStartListener = onTimerStartListener;
        return this;
    }

    public synchronized UiTimer setUiTimerExecuteBody(UiTimerExecuteBody uiTimerExecuteBody) {
        this.uiTimerExecuteBody = uiTimerExecuteBody;
        return this;
    }

    public synchronized UiTimer setOnTimerFinishListener(OnTimerFinishListener onTimerFinishListener) {
        this.onTimerFinishListener = onTimerFinishListener;
        return this;
    }

    public TimerDriver getTimerDriver() {
        return timerDriver;
    }

    /**
     * 当定时器启动时的回调
     */
    public interface OnTimerStartListener {
        void onTimerStart(UiTimer uiTimer, TimerDriver timerDriver);
    }

    /**
     * 任务执行体
     */
    public interface UiTimerExecuteBody {
        void onExecute(UiTimer timer, TimerDriver timerDriver);
    }

    /**
     * 当任务结束时的回调
     */
    public interface OnTimerFinishListener {
        void onTimerFinish(UiTimer timer, TimerDriver timerDriver);
    }

    /**
     * 定时器驱动接口
     */
    public interface TimerDriver {
        void startDrive(UiTimer uiTimer);

        void stopDrive(UiTimer uiTimer);
    }

    /**
     * 默认的定时器驱动器
     * 无限制的按照时间间隔执行
     */
    public static class DefaultTimerDriver implements TimerDriver {
        private TimerHandler timerHandler;

        @Override
        public void startDrive(final UiTimer uiTimer) {
            if (timerHandler != null) {
                timerHandler.stopTimer();
            }
            timerHandler = new TimerHandler(uiTimer, new Runnable() {
                public void run() {
                    if (uiTimer.uiTimerExecuteBody != null) {
                        uiTimer.uiTimerExecuteBody.onExecute(uiTimer, DefaultTimerDriver.this);
                    }
                }
            });
            timerHandler.startTimer();
        }

        @Override
        public void stopDrive(UiTimer uiTimer) {
            if (timerHandler != null) {
                timerHandler.stopTimer();
            }
        }
    }

    public static class NumberTimerDriver implements TimerDriver {
        private TimerHandler timerHandler;
        private int startNumber;
        private int endNumber;
        private int currentNumber;
        private int step = 1;
        private boolean loop;
        private boolean reverse;

        public NumberTimerDriver(int startNumber, int endNumber, int step, boolean loop, boolean reverse) {
            this.startNumber = startNumber;
            this.endNumber = endNumber;
            this.step = step;
            this.loop = loop;
            this.reverse = reverse;
            this.initCurrentNumber();
        }

        private void reverseStatusIfNeed() {
            if (this.reverse) {
                int temp = this.startNumber;
                this.startNumber = this.endNumber;
                this.endNumber = temp;
            }
        }

        public void initCurrentNumber() {
            this.currentNumber = this.startNumber;
        }

        private void optCurrentNumber() {
            this.currentNumber += this.startNumber < this.endNumber ? this.step : -this.step;
        }

        private boolean shouldStop() {
            return this.startNumber < this.endNumber ? this.currentNumber > this.endNumber : this.currentNumber < this.endNumber;
        }

        public int getCurrentNumber() {
            return currentNumber;
        }

        private void timerExec(UiTimer uiTimer) {
            if (uiTimer.uiTimerExecuteBody != null && !shouldStop()) {
                uiTimer.uiTimerExecuteBody.onExecute(uiTimer, NumberTimerDriver.this);
            }
            if (shouldStop()) {
                if (loop) {
                    reverseStatusIfNeed();
                    initCurrentNumber();
                    timerExec(uiTimer);
                } else {
                    uiTimer.cancle();
                }
            } else {
                optCurrentNumber();
            }
        }

        @Override
        public void startDrive(final UiTimer uiTimer) {
            if (timerHandler != null) {
                timerHandler.stopTimer();
            }
            timerHandler = new TimerHandler(uiTimer, new Runnable() {
                public void run() {
                    timerExec(uiTimer);
                }
            });
            timerHandler.startTimer();
        }

        @Override
        public void stopDrive(UiTimer uiTimer) {
            if (timerHandler != null) {
                timerHandler.stopTimer();
            }
        }
    }

    /**
     * 定时执行任务的简单封装，用于提供最底层的间隔执行能力
     */
    private static class TimerHandler extends Handler {
        private UiTimer uiTimer;
        private int identityId;
        private Runnable timerExecuteBody;

        public TimerHandler(UiTimer uiTimer, Runnable timerExecuteBody) {
            super(Looper.getMainLooper());
            this.uiTimer = uiTimer;
            this.identityId = hashCode();
            this.timerExecuteBody = timerExecuteBody;
        }

        public void startTimer() {
            sendMessage(obtainMessage(identityId));
        }

        public void stopTimer() {
            removeMessages(identityId);
        }

        public void handleMessage(Message msg) {
            if (msg.what == identityId && uiTimer.isSchedule && timerExecuteBody != null) {
                timerExecuteBody.run();
                sendMessageDelayed(obtainMessage(identityId), uiTimer.delay);
            }
        }
    }
}
