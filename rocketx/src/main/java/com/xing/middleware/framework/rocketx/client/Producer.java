package com.xing.middleware.framework.rocketx.client;

import com.alibaba.fastjson.JSON;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class Producer implements InitializingBean, DisposableBean {

    protected String producerGroup;
    protected DefaultMQProducer producer;
    protected MessageQueueSelector messageQueueSelector;

    public Producer(String producerGroup, String namesrvAddr) {
        this(producerGroup, namesrvAddr, 1, 2, null);
    }

    public Producer(String producerGroup, String namesrvAddr, MessageQueueSelector messageQueueSelector) {
        this(producerGroup, namesrvAddr, 1, 2, messageQueueSelector);
    }

    public Producer(String producerGroup, String namesrvAddr, int retryTimes,
                    int defaultTopicQueueNums, MessageQueueSelector messageQueueSelector) {
        this.producerGroup = producerGroup;
        this.messageQueueSelector = messageQueueSelector;
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimes);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.setMaxMessageSize(65535);
        producer.setDefaultTopicQueueNums(defaultTopicQueueNums);
    }

    public <T> SendResult syncSend(String topic, String messageKey, T body) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        byte[] bytes = JSON.toJSONString(body).getBytes(RemotingHelper.DEFAULT_CHARSET);
        Message msg = new Message(topic, "", messageKey, bytes);
        if (messageQueueSelector == null) {
            return producer.send(msg);
        }
        return producer.send(msg, messageQueueSelector, null);
    }

    public <T> void asyncSend(String topic, T body, String messageKey, SendCallback sendCallback) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        byte[] bytes = JSON.toJSONString(body).getBytes(RemotingHelper.DEFAULT_CHARSET);
        Message msg = new Message(topic, "", messageKey, bytes);
        if (messageQueueSelector == null) {
            producer.send(msg, sendCallback);
            return;
        }
        producer.send(msg, messageQueueSelector, null, sendCallback);
    }


    @Override
    public void destroy() throws Exception {
        producer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        producer.start();
    }
}
