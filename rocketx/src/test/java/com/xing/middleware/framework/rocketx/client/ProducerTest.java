package com.xing.middleware.framework.rocketx.client;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.xing.middleware.framework.rocketx.client.pojo.Order;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class ProducerTest extends TestCase {
    public void testProducer() throws Exception {
        Producer p = new Producer("ProducerGroup", "1139.224.137.97:9876;139.224.137.97:9876");
        p.afterPropertiesSet();
        Order order = new Order();
        order.setOrderId("order-111");
        order.setMessage("order message");
        for (int i = 0; i < 10; i++) {
            SendResult sendResult = p.syncSend("fxTestTopic", "order-" + Integer.toString(i), order);
            Assert.assertTrue(sendResult.getSendStatus().equals(SendStatus.SEND_OK));
        }
        p.destroy();
    }

}
