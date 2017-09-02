package com.xing.middleware.framework.rocketx.client.pojo;

/**
 * Created by Jecceca on 2017/8/29.
 */
public class Order {
    private String orderId;
    private String message;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
