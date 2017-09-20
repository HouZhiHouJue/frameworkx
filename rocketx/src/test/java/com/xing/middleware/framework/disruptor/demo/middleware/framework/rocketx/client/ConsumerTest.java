package com.xing.middleware.framework.disruptor.demo.middleware.framework.rocketx.client;

import com.xing.middleware.framework.disruptor.demo.middleware.framework.rocketx.client.handler.MessageListenerOrderlyHandler;
import junit.framework.TestCase;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class ConsumerTest extends TestCase {
    public void testConsumer() throws Exception {
        Consumer consumer = new Consumer("ConsumerGroup", "1139.224.137.97:9876;139.224.137.97:9876",
                new MessageListenerOrderlyHandler());
        consumer.afterPropertiesSet();
        Thread.sleep(60 * 1000);
        consumer.destroy();
    }
}
