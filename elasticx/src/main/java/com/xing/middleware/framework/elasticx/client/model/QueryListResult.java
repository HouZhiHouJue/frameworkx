package com.xing.middleware.framework.elasticx.client.model;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class QueryListResult<T extends Object> extends BaseQueryResult {
    private Hits<T> hits;

    public Hits<T> getHits() {
        return hits;
    }

    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }
}
