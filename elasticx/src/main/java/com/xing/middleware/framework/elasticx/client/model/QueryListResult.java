package com.xing.middleware.framework.elasticx.client.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class QueryListResult<T extends Object> {
    @JSONField(name = "_shards")
    private Shards shards;
    @JSONField(name = "timed_out")
    private boolean timeout;
    private int took;
    private Hits<T> hits;

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

    public Hits<T> getHits() {
        return hits;
    }

    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }
}
