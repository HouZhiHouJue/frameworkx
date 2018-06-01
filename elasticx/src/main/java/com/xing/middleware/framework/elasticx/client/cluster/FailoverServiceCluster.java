package com.xing.middleware.framework.elasticx.client.cluster;


import com.xing.middleware.framework.common.Utils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;

import static org.asynchttpclient.Dsl.*;

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
        asyncHttpClient = asyncHttpClient(config()
                .setConnectTimeout(1 * 1000).setReadTimeout(2 * 1000).setRequestTimeout(5 * 1000)
                .setMaxConnections(10).setMaxConnectionsPerHost(2).setKeepAlive(true)
                .setConnectionTtl(45*1000).setFollowRedirect(true).setMaxRedirects(2)
                .setMaxRequestRetry(2).build());
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
