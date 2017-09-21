package com.xing.middleware.framework.elasticx.client;

import com.alibaba.druid.pool.ElasticSearchDruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import com.xing.middleware.framework.common.Utils;
import com.xing.middleware.framework.elasticx.client.cluster.FailoverServiceCluster;
import com.xing.middleware.framework.elasticx.client.cluster.ServiceCluster;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class ElasticxClient implements InitializingBean, DisposableBean {

    protected ElasticSearchDruidDataSource elasticSearchDruidDataSource;
    protected static SerializeConfig serializeConfig = new SerializeConfig();
    protected static final String DEFAULT_TYPE = "default";
    protected static final String FAST_JSON_ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    protected TransportClient client;
    protected String[] servers;
    protected ServiceCluster serviceCluster;

    static {
        JSON.defaultTimeZone = TimeZone.getTimeZone("UTC");
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(FAST_JSON_ES_DATE_FORMAT));
    }

    public ElasticxClient(String servers,ElasticSearchDruidDataSource druidDataSource) {
        this.servers = servers.split(",");
        this.elasticSearchDruidDataSource = druidDataSource;
    }

    public <T> boolean save(String indexName, T data) {
        String json = JSON.toJSONString(data, serializeConfig);
        indexName = String.format("cu-%s-%s", indexName, Utils.formatUtcDate(Calendar.getInstance().getTime()));
        IndexResponse response = client.prepareIndex(indexName, DEFAULT_TYPE)
                //.setRouting(routingKey)
                .setSource(json)
                .execute()
                .actionGet();
        return true;
    }

    public <T> boolean batchSave(String indexName, List<T> batchData) {
        indexName = String.format("cu-%s-%s", indexName, Utils.formatUtcDate(Calendar.getInstance().getTime()));
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (T data : batchData) {
            String json = JSON.toJSONString(data, serializeConfig);
            bulkRequest.add(client.prepareIndex(indexName, DEFAULT_TYPE)
                    //.setRouting(routingKey)
                    .setSource(json));
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            return false;
        }
        return true;
    }

    /**
     * http request -> string -> parse to T
     * @param sql
     * @param typeReference
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T query(String sql, TypeReference<T> typeReference) throws Exception {
        String s = serviceCluster.query(sql);
        if (Utils.isNullOrEmpty(s)) return null;
        return JSON.parseObject(s, typeReference);
    }

    /**
     * http request -> string
     * @param sql
     * @return
     * @throws Exception
     */
    public String query(String sql) throws Exception {
        String s = serviceCluster.query(sql);
        return s;
    }

    /**
     * tcp request -> jdbc protocol -> use dbutils to parse
     * @param sql
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> query(String sql, Class<T> clazz) throws Exception {
        QueryRunner qr = new QueryRunner(elasticSearchDruidDataSource);
        List<T> datas = qr.query(sql, new BeanListHandler<T>(clazz));
        return datas;
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
        serviceCluster = new FailoverServiceCluster(1, servers);
    }
}
