package com.amaan.dubbo.service;

import com.amaan.dubbo.MallOrder;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-12 16:31
 */
public interface MallOrderService {
    MallOrder getOrderById(String orderId);
    Integer saveOrder(MallOrder order);
}
