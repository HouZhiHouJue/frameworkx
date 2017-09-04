package com.xing.middleware.framework.rocketx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
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
        this(producerGroup, namesrvAddr, 2, 4, messageQueueSelector);
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
