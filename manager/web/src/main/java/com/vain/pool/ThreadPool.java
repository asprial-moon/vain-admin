package com.vain.pool;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author vain
 * @Description
 * @date 2018/9/8 16:39
 */
@Component
public class ThreadPool {

    /**
     * 针对核心Thread的Max比率 ，以10为基数，8表示0.8
     */
    private int notifyRadio = 8;
    /**
     * 最少线程数
     */
    private int corePoolSize = 2;
    /**
     * 线程池缓冲队列大小.
     */
    private int workQueueSize = 100;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 5;
    /**
     * 允许线程闲置时间,单位：秒
     */
    private long keepAliveTime = 300;

    private ThreadPoolExecutor executor = null;

    @PostConstruct
    public void init() {
        if (workQueueSize < 1) {
            workQueueSize = 1000;
        }
        if (this.keepAliveTime < 1) {
            this.keepAliveTime = 1000;
        }
        int coreSize = 0;
        if (this.corePoolSize < 1) {
            coreSize = Runtime.getRuntime().availableProcessors();
            maxPoolSize = Math.round(((float) (coreSize * notifyRadio)) / 10);
            corePoolSize = coreSize / 4;
            if (corePoolSize < 1) {
                corePoolSize = 1;
            }
        }

        // corePoolSize不能大于maxPoolSize，否则会出错
        if (maxPoolSize < corePoolSize) {
            maxPoolSize = corePoolSize;
        }

        BlockingQueue<Runnable> notifyWorkQueue = new ArrayBlockingQueue<>(workQueueSize);

        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, notifyWorkQueue, new ThreadPoolExecutor.DiscardOldestPolicy());

    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }
}
