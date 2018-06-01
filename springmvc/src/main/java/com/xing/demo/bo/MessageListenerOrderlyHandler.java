package com.xing.demo.bo;

import com.alibaba.fastjson.JSON;
import com.xing.demo.model.Order;
import com.xing.middleware.framework.rocketx.client.TopicHandler;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Jecceca on 2017/9/4.
 */
@TopicHandler(topic = "fxTestTopic")
public class MessageListenerOrderlyHandler implements MessageListenerOrderly {
    AtomicLong counter = new AtomicLong();

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt messageExt : msgs) {
            Order order = JSON.parseObject(new String(messageExt.getBody(), StandardCharsets.UTF_8), Order.class);
            counter.incrementAndGet();
            System.out.print(counter.get() + "\n");
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
