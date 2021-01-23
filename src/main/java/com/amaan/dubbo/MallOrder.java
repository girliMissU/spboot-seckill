package com.amaan.dubbo;

import java.io.Serializable;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-12 16:32
 */
public class MallOrder implements Serializable {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "MallOrder{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
