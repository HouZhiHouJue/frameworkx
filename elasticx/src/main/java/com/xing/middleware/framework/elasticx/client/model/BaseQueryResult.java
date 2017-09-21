package com.xing.middleware.framework.elasticx.client.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Jecceca on 2017/9/20.
 */
public class BaseQueryResult {
    @JSONField(name = "_shards")
    protected Shards shards;
    @JSONField(name = "timed_out")
    protected boolean timeout;
    protected int took;

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

}
