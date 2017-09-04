package com.xing.middleware.framework.elasticx.client.cluster;

import com.alibaba.fastjson.TypeReference;

/**
 * Created by Jecceca on 2017/9/4.
 */
public interface ServiceCluster {
    <T> T query(String sql,TypeReference<T> typeReference) throws Exception;
}
