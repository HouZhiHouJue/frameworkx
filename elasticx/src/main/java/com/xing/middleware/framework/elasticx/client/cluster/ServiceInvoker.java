package com.xing.middleware.framework.elasticx.client.cluster;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class ServiceInvoker {
    public static <T> T get(AsyncHttpClient asyncHttpClient, String url, TypeReference<T> typeReference) throws ExecutionException, InterruptedException, IOException {
        Request request = new RequestBuilder("GET").setUrl(url)
                .setBodyEncoding("UTF-8")
                .build();
        Response response = asyncHttpClient.executeRequest(request).get();
        if (response.getStatusCode() == 200) {
            return JSON.parseObject(response.getResponseBody(), typeReference);
        }
        return null;
    }
}
