package com.xing.middleware.framework.disruptor.demo.middleware.framework.elasticx.client.cluster;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.xing.middleware.framework.disruptor.demo.middleware.framework.common.Utils;

import java.util.Random;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class FailoverServiceCluster implements ServiceCluster {
    protected int reTry;
    protected String[] servers;
    protected AsyncHttpClient asyncHttpClient;
    protected Random random;

    public FailoverServiceCluster(int maxRetry, String[] servers) {
        this.reTry = maxRetry + 1;
        random = new Random(System.currentTimeMillis());
        this.servers = servers;
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        AsyncHttpClientConfig asyncHttpClientConfig = builder
                .setConnectTimeout(2 * 1000)
                .setRequestTimeout(5 * 1000)
                .setReadTimeout(5 * 1000)
                .setMaxConnectionsPerHost(20)
                .setMaxConnections(100)
                .setAllowPoolingConnections(true)
                .setPooledConnectionIdleTimeout(45 * 1000)
                .build();
        asyncHttpClient = new AsyncHttpClient(asyncHttpClientConfig);
    }

    @Override
    public String query(String sql) throws Exception {
        int index = random.nextInt(servers.length);
        for (int i = 0; i < reTry; i++) {
            String server = servers[index];
            String url = String.format("http://%s:9700/_sql?sql=%s", server, sql);
            try {
                String s = ServiceInvoker.get(this.asyncHttpClient, url);
                if (!Utils.isNullOrEmpty(s)) return s;
            } catch (Exception ex) {
                if (i == reTry - 1)
                    throw ex;
            }
        }
        return null;
    }
}
