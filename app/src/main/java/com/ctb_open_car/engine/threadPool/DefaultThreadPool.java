package com.ctb_open_car.engine.threadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

public class DefaultThreadPool {
    private static ThreadPoolProxy mLongPool = null;
    private static ThreadPoolProxy mDownloadPool = null;
    private static ThreadPoolProxy mUpLoadPoll = null;

    private static Object mDownloadLock = new Object();
    private static Object mFrquentlyLock = new Object();
    public static Object mUpLoadPollLock = new Object();

    /**
     * 下载,支持5个并行任务
     */
    public static ThreadPoolProxy getDownloadPool() {
        synchronized (mDownloadLock) {
            if (mDownloadPool == null) {
                mDownloadPool = new ThreadPoolProxy(5, 5, 5L, new LinkedBlockingQueue<Runnable>());
            }
            return mDownloadPool;
        }
    }

    /**
     * 上传，支持5个并行任务
     */
    public static ThreadPoolProxy getUploadPool() {
        synchronized (mUpLoadPollLock) {
            if (mUpLoadPoll == null) {
                mUpLoadPoll = new ThreadPoolProxy(5, 5, 5L, new LinkedBlockingQueue<Runnable>());
            }
            return mUpLoadPoll;
        }
    }

    /**
     * 做大量的短耗时任务，列如：文件操作，网络操作,数据库操作等
     */
    public static ThreadPoolProxy getMaxPool() {
        synchronized (mFrquentlyLock) {
            if (mLongPool == null) {
                mLongPool = new ThreadPoolProxy(0, 1024, 10, new SynchronousQueue<Runnable>());
            }
            return mLongPool;
        }
    }

    /**
     * 移除所有的任务，退出的时候用
     */
    public static void removeAllTask() {
        if (mLongPool != null) {
            mLongPool.removeAllTask();
            mLongPool.stop();
        }
        if (mDownloadPool != null) {
            mDownloadPool.removeAllTask();
            mDownloadPool.stop();
        }
        if (mUpLoadPoll != null) {
            mUpLoadPoll.removeAllTask();
            mUpLoadPoll.stop();
        }
    }

    public static class ThreadPoolProxy {
        private ThreadPoolExecutor mPool;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;
        private BlockingQueue<Runnable> mWorkQueue;

        /**
         * 线程池构造方法中，赋值最小线程数，最大线程数，空闲存活时间
         */
        private ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> workQueue) {
            mCorePoolSize = corePoolSize;
            mMaximumPoolSize = maximumPoolSize;
            mKeepAliveTime = keepAliveTime;
            mWorkQueue = workQueue;
        }

        /**
         * 执行任务，当线程池处于关闭，将会重新创建新的线程池
         */
        public synchronized void execute(Runnable run) {
            if (run == null) {
                return;
            }
            if (mPool == null || mPool.isShutdown()) {
                mPool = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, TimeUnit.SECONDS, mWorkQueue, Executors.defaultThreadFactory(), new AbortPolicy());
            }
            mPool.execute(run);
        }

        /**
         * 取消线程池中某个还未执行的任务
         */
        public synchronized void cancel(Runnable run) {
            if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                mPool.getQueue().remove(run);
            }
        }

        /**
         * 取消线程池中某个还未执行的任务
         */
        public synchronized boolean contains(Runnable run) {
            if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                return mPool.getQueue().contains(run);
            } else {
                return false;
            }
        }

        /**
         * 删除队列中所有的任务
         */
        public synchronized void removeAllTask() {
            if (mWorkQueue != null && mWorkQueue.size() > 0) {
                mWorkQueue.clear();
            }
        }

        /**
         * 立刻关闭线程池，并且正在执行的任务也将会被中断
         */
        public void stop() {
            if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                mPool.shutdownNow();
            }
        }

        /**
         * 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭
         */
        public synchronized void shutdown() {
            if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                mPool.shutdown();
            }
        }
    }
}

