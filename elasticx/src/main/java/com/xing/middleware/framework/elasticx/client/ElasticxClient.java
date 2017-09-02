package com.xing.middleware.framework.elasticx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.xing.middleware.framework.common.Utils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Jecceca on 2017/8/28.
 */
@Component
public class ElasticxClient implements InitializingBean, DisposableBean {
    private static SerializeConfig serializeConfig = new SerializeConfig();
    private static final String FAST_JSON_ES_DATE_FORMAT;
    private static final String DEFAULT_TYPE = "default";
    private TransportClient client;
    private Set<String> servers;

    static {
        JSON.defaultTimeZone =  TimeZone.getTimeZone("UTC");
        FAST_JSON_ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(FAST_JSON_ES_DATE_FORMAT));
    }

    public ElasticxClient(Set<String> servers) {
        this.servers = servers;
    }

    public <T> boolean save(String indexName, String routingKey, T data) {
        String json = JSON.toJSONString(data, serializeConfig);
        indexName = indexName + "-" + Utils.formatUtcDate(Calendar.getInstance().getTime());
        IndexResponse response = client.prepareIndex(indexName, DEFAULT_TYPE)
                .setRouting(routingKey)
                .setSource(json)
                .execute()
                .actionGet();
        return true;
    }

    public <T> boolean batchSave(String indexName, String routingKey, List<T> batchData) {
        indexName = indexName + "-" + Utils.formatUtcDate(Calendar.getInstance().getTime());
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (T data : batchData) {
            bulkRequest.add(client.prepareIndex(indexName, DEFAULT_TYPE)
                    .setRouting(routingKey)
                    .setSource(JSON.toJSONString(data, serializeConfig)));
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            return false;
        }
        return true;
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("client.transport.ignore_cluster_name", true).build();// .put("cluster.name", clusterName)
        client = new TransportClient(settings);
        for (String server : servers) {
            client = client.addTransportAddress(new InetSocketTransportAddress(server, 9800));
        }
    }
}
