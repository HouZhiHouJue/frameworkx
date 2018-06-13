package com.xing.middleware.framework.rocketx.client;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .followRedirects(true)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        for (int i = 0; i < 10000; i++) {
            Response response = client.newCall(request).execute();
            // Deserialize HTTP response to concrete type.
            ResponseBody body = response.body();
            System.out.print(body.string());
        }
        System.out.print("over");
        System.in.read();
    }
}
