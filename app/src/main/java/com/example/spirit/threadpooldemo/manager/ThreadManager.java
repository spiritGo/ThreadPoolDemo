package com.example.spirit.threadpooldemo.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static ThreadPool mThreadPool;

    public static ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            synchronized (ThreadPool.class) {
                if (mThreadPool == null) {

                    //int cpuCount = Runtime.getRuntime().availableProcessors();//cpu数量
                    //int threadCount = cpuCount * 2 + 1;
                    int threadCount = 10;

                    mThreadPool = new ThreadPool(threadCount, threadCount, 1L);
                }
            }
        }

        return mThreadPool;
    }

    //线程池
    public static class ThreadPool {

        private int corePoolSize;//核心线程数
        private int maximumPoolSize;//最大线程数
        private long keepAliveTime;//休息时间
        private TimeUnit unit = TimeUnit.SECONDS;//时间的单位
        private LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();//线程队列
        private ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂

        //线程异常处理策略
        private ThreadPoolExecutor.AbortPolicy handler = new ThreadPoolExecutor.AbortPolicy();
        private ThreadPoolExecutor executor;

        ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        keepAliveTime, unit, workQueue, threadFactory, handler);
            }

            //执行一个runnable对象
            executor.execute(r);
        }

        public void cancel(Runnable runnable) {
            if (executor != null) {
                //从线程队列中移除对象
                executor.getQueue().remove(runnable);
            }
        }
    }
}
