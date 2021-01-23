package com.amaan.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * 佛祖保佑，永无BUG
 * 使用Curator实现分布式master选举
 * 选择一个根节点，多台机器同时向给节点创建一个子节点，只有一个能创建成功--master
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-13 19:21
 */
public class MasterSelect {
    private static String masterPath = "/curator_recipes_master_path";
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.182:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        LeaderSelector select = new LeaderSelector(client, masterPath,
                //curator会在竞争到Master后自动调用该方法，需根据业务实现
                new LeaderSelectorListenerAdapter() {
                    @Override
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println("成为Master角色");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("完成Mater操作，释放Master权力");
                    }
                });
        select.autoRequeue();
        select.start();
        TimeUnit.MILLISECONDS.sleep(Integer.MAX_VALUE);
    }
}
