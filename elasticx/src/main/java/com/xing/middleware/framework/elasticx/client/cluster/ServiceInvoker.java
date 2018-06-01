package com.xing.middleware.framework.elasticx.client.cluster;


import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jecceca on 2017/9/4.
 */
public class ServiceInvoker {
    public static String get(AsyncHttpClient asyncHttpClient, String url) throws ExecutionException, InterruptedException, IOException {
        Request request = new RequestBuilder("GET").setUrl(url)
                .build();
        Response response = asyncHttpClient.executeRequest(request).get();
        if (response.getStatusCode() == 200) {
            return response.getResponseBody();
        }
        return null;
    }
}
