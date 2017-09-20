package com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class Hits<T extends Object> {
    @JSONField(name = "hits")
    private List<Hit<T>> hits;
    @JSONField(name = "max_score")
    private int maxScore;
    private int total;

    public List<Hit<T>> getHits() {
        return hits;
    }

    public void setHits(List<Hit<T>> hits) {
        this.hits = hits;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
