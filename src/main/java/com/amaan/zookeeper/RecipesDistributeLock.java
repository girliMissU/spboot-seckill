package com.amaan.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 佛祖保佑，永无BUG
 * 使用Curator实现分布式锁功能
 * 模拟生成订单号
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-13 19:31
 */
public class RecipesDistributeLock {
    private static String lockPath = "/curator_recipes_lock_path";
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.182.128:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        final CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 30; i++) {
            service.execute(() -> {
                try {
                    latch.await();
                    lock.acquire();
                } catch (Exception ignored) {
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                System.out.println("生成的订单号："+orderNo);
                try {
                    lock.release();
                } catch (Exception ignored) {
                }
            });
        }
        latch.countDown();
        service.shutdown();
    }
}
