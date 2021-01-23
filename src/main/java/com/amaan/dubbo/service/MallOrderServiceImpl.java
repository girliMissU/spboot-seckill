package com.amaan.dubbo.service;

import com.amaan.dubbo.MallOrder;
//import com.amaan.dubbo.service.MallOrderService;
//import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-01-12 16:34
 */
//@Component//交给IOC
//@DubboService(version = "1.0.0", interfaceName = "mallOrder service")
public class MallOrderServiceImpl implements MallOrderService {

//    @Autowired
//    private MallOrderMapper orderMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public MallOrder getOrderById(String orderId) {
//        return orderMapper.selectByPrimaryKey(orderId);
        logger.info("getOrderById()调用"+orderId);
        MallOrder order = new MallOrder();
        order.setOrderId(orderId);
        return order;
    }

    @Override
    public Integer saveOrder(MallOrder order) {
        logger.info("saveOrder()调用"+order);
//        return orderMapper.insert(order);
        return 1;
    }
}
