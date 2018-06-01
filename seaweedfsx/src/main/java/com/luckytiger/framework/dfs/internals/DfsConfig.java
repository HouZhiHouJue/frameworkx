package com.luckytiger.framework.dfs.internals;

import lombok.Getter;
import lombok.Setter;
import org.asynchttpclient.AsyncHttpClient;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

@Getter
@Setter
public class DfsConfig {
    private int requestTimeout = 10 * 1000;
    private int readTimeout = requestTimeout;
    private int connectionTimeout = requestTimeout / 10;
    private int maxConnections = 100;
    private int maxConnectionsPerHost = 20;
    private int connectionTtl = 45 * 1000;
    private int maxRetry = 1;
    private String serverAddr;
    private String fileTtl;

}
