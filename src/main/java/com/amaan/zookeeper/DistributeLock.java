package com.amaan.zookeeper;

import com.google.common.base.Strings;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 佛祖保佑，永无BUG
 * Zookeeper实现分布式锁
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-12 20:26
 */
//@Component
public class DistributeLock {

    private String zkQurom = "192.168.182.128:2181";

    private String lockNameSpace = "/mylock";

    private String nodeString = lockNameSpace + "/test1";

    private ZooKeeper zk;

    public DistributeLock() {
        try {
            //创建会话,异步
            zk = new ZooKeeper(zkQurom, 6000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("Receive event " + watchedEvent);
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                        System.out.println("connection is established...");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureRootPath() throws InterruptedException {
        try {
            if (zk.exists(lockNameSpace, true) == null) {
                //创建数据节点,同步
                zk.create(lockNameSpace, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private void watchNode(String nodeString, final Thread thread) throws InterruptedException {
        try {
            zk.exists(nodeString, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("==" + watchedEvent.toString());
                    if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                        System.out.println("There was a Thread released Lock==============");
                        thread.interrupt();
                    }
                    //watcher是一次性的，需反复注册
                    try {
                        zk.exists(nodeString, new Watcher() {
                            @Override
                            public void process(WatchedEvent watchedEvent) {
                                System.out.println("==" + watchedEvent.toString());
                                if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                                    System.out.println("There was a Thread released Lock==============");
                                    thread.interrupt();
                                }
                                try {
                                    zk.exists(nodeString, true);
                                } catch (KeeperException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取锁
     * @return
     * @throws InterruptedException
     */
    public boolean lock() throws InterruptedException {
        String path = null;
        ensureRootPath();
        watchNode(nodeString, Thread.currentThread());
        while (true) {
            try {
                path = zk.create(nodeString, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            } catch (KeeperException e) {
                System.out.println(Thread.currentThread().getName() + "  getting Lock but can not get");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.out.println("thread is notify");
                }
            }
            if (!Strings.nullToEmpty(path).trim().isEmpty()) {
                System.out.println(Thread.currentThread().getName() + "  get Lock...");
                return true;
            }
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        try {
            zk.delete(nodeString, -1);
            System.out.println(Thread.currentThread().getName() + " release Lock...");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 4; i++) {
            service.execute(() -> {
                DistributeLock test = new DistributeLock();
                try {
                    test.lock();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.unlock();
            });
        }
        service.shutdown();
    }
}

