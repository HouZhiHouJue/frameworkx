package com.xing.middleware.framework.elasticx.client.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class Hit<T extends Object> {
    @JSONField(name = "_id")
    private String id;
    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_score")
    private int score;
    @JSONField(name = "_source")
    private T source;
    @JSONField(name = "_type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
