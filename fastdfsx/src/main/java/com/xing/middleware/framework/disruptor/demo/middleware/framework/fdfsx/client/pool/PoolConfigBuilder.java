package com.xing.middleware.framework.disruptor.demo.middleware.framework.fdfsx.client.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by Jecceca on 2017/9/3.
 */
public class PoolConfigBuilder {

    public static GenericObjectPoolConfig buildDefault() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(4);
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxWaitMillis(3000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(5 * 60);
        poolConfig.setNumTestsPerEvictionRun(2);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(30 * 1000);
        return poolConfig;
    }
}
