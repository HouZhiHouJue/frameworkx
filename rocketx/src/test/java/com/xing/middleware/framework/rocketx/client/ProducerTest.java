package com.xing.middleware.framework.rocketx.client;



import com.xing.middleware.framework.rocketx.client.model.Order;
import junit.framework.TestCase;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.junit.Assert;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class ProducerTest extends TestCase {
    public void testProducer() throws Exception {
        Producer p = new Producer("ProducerGroup", "139.224.137.97:9876;139.224.137.97:9876");
        p.afterPropertiesSet();
        Order order = new Order();
        order.setOrderId("order-111");
        order.setMessage("order message");
        for (int i = 0; i < 10; i++) {
            try {
                SendResult sendResult = p.syncSend("fxTestTopic", "order-" + Integer.toString(i), order);
                System.out.print(sendResult.getSendStatus() + "\n");
                Assert.assertTrue(sendResult.getSendStatus().equals(SendStatus.SEND_OK));
                Thread.sleep(5000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        p.destroy();
    }

}
