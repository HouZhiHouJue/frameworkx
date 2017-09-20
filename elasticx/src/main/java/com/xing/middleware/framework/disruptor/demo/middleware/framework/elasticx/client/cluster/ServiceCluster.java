package com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client.cluster;

/**
 * Created by Jecceca on 2017/9/4.
 */
public interface ServiceCluster {
    String query(String sql) throws Exception;
}
