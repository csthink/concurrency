package com.csthink.concurrency;

import com.csthink.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 并发模拟测试
 */
@Slf4j
@NotThreadSafe
public class ConcurrencyTest {

    // 请求总数
    private static int clientTotal = 5000;

    // 同时并发执行的线程数
    private static int threadTotal = 200;

    private static int count = 0;

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 获取信号量
        final Semaphore semaphore = new Semaphore(threadTotal);
        // 获取计数器闭锁
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("count:{}", count);
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
            executorService.shutdown();
            log.info("count:{}", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void add() {
        count++;
    }
}
