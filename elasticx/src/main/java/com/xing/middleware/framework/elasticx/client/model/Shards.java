package com.xing.middleware.framework.elasticx.client.model;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class Shards {
    private int failed;
    private int successful;
    private int total;

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSuccessful() {
        return successful;
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
