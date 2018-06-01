package com.xing.middleware.framework.rocketx.client;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class Consumer implements InitializingBean, DisposableBean {
    protected DefaultMQPushConsumer consumer;
    protected String namesrvAddr;
    protected String consumerGroup;
    protected MessageListener messageListener;

    public Consumer(String consumerGroup, String namesrvAddr, MessageListener messageListener) {
        this.consumerGroup = consumerGroup;
        this.namesrvAddr = namesrvAddr;
        this.messageListener = messageListener;
    }

    protected void subscribe(String topic, String subExpression, MessageListener messageListener) throws Exception {
        consumer.subscribe(topic, subExpression);
        consumer.registerMessageListener(messageListener);
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
        consumer.setConsumeTimeout(100);
        consumer.setMaxReconsumeTimes(3);
        TopicHandler topicHandler = messageListener.getClass().getAnnotation(TopicHandler.class);
        subscribe(topicHandler.topic(), topicHandler.subExpression(), messageListener);
        consumer.start();
    }
}
