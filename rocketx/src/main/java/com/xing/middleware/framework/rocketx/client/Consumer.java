package com.xing.middleware.framework.rocketx.client;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by Jecceca on 2017/8/29.
 */
@Component
public class Consumer implements InitializingBean, DisposableBean {
    private DefaultMQPushConsumer consumer;
    private String namesrvAddr;
    private String consumerGroup;

    public Consumer(String consumerGroup, String namesrvAddr) {
        this.consumerGroup = consumerGroup;
        this.namesrvAddr = namesrvAddr;
    }

    public Consumer subscribe(String topic, String subExpression, MessageListenerOrderly messageListenerOrderly) throws Exception {
        consumer.subscribe(topic, subExpression);
        consumer.registerMessageListener(messageListenerOrderly);
        return this;
    }

    public Consumer subscribe(String topic, String subExpression, MessageListenerConcurrently messageListenerConcurrently) throws Exception {
        consumer.subscribe(topic, subExpression);
        consumer.registerMessageListener(messageListenerConcurrently);
        return this;
    }

    public void start() throws MQClientException {
        consumer.start();
    }

    @Override
    public void destroy() throws Exception {
        consumer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer(this.consumerGroup);
        consumer.setNamesrvAddr(this.namesrvAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setConsumeTimeout(30);
        consumer.setMaxReconsumeTimes(3);
    }
}
