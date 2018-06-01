package com.luckytiger.framework.dfs.internals;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.google.common.base.Preconditions;
import com.luckytiger.framework.dfs.DfsClient;
import com.luckytiger.framework.dfs.FileInfo;
import com.luckytiger.framework.dfs.UploadResult;
import com.luckytiger.framework.dfs.internals.Exception.HttpException;
import com.luckytiger.framework.dfs.internals.cst.ConfigConst;
import com.luckytiger.framework.dfs.internals.dto.FileAssign;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.ByteArrayPart;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

@Slf4j
public class DfsClientImpl implements InitializingBean, DisposableBean, DfsClient {
    private AsyncHttpClient asyncHttpClient;
    private DfsConfig dfsConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = ConfigService.getConfig(ConfigConst.DFS_PUBLIC_NAMESPACE);
        DfsConfigParser.init(config);
        dfsConfig = DfsConfigParser.getDfsConfig();
        this.asyncHttpClient = asyncHttpClient(config()
                .setConnectTimeout(dfsConfig.getConnectionTimeout())
                .setReadTimeout(dfsConfig.getReadTimeout())
                .setRequestTimeout(dfsConfig.getReadTimeout())
                .setMaxConnections(dfsConfig.getMaxConnections())
                .setMaxConnectionsPerHost(dfsConfig.getMaxConnectionsPerHost())
                .setConnectionTtl(dfsConfig.getConnectionTtl())
                .setKeepAlive(true)
                .setFollowRedirect(true)
                .setMaxRedirects(2)
                .setMaxRequestRetry(2)
                .build());
    }

    @Override
    public void destroy() throws Exception {
        this.asyncHttpClient.close();
    }

    @Override
    public UploadResult upload(FileInfo fileInfo) throws ExecutionException, InterruptedException, HttpException {
        return upload(fileInfo, dfsConfig.getRequestTimeout(), dfsConfig.getMaxRetry());
    }

    @Override
    public UploadResult upload(FileInfo fileInfo, int requestTimeout) throws InterruptedException, ExecutionException, HttpException {
        return upload(fileInfo, requestTimeout, dfsConfig.getMaxRetry());
    }

    @Override
    public UploadResult upload(FileInfo fileInfo, int requestTimeout, int maxRetry) throws HttpException, ExecutionException, InterruptedException {
        Preconditions.checkNotNull(fileInfo);
        Preconditions.checkNotNull(fileInfo.getFileName());
        Preconditions.checkNotNull(fileInfo.getBytes());
        Preconditions.checkArgument(fileInfo.getBytes().length > 0);
        UploadResult uploadResult = new UploadResult();
        String url = String.format(ConfigConst.MASTER_URL, dfsConfig.getMasterAddr());
        if (!dfsConfig.getFileTtl().equals("-1")) {
            url = String.format("%s?ttl=%s", url, dfsConfig.getFileTtl());
        }
        for (int i = 0; i < maxRetry + 1; i++) {

            Request request = new RequestBuilder("GET")
                    .setRequestTimeout(requestTimeout)
                    .setUrl(url)
                    .build();
            Response response = asyncHttpClient.executeRequest(request).get();
            Utility.raiseForStatus(response);
            FileAssign fileAssign = JSON.parseObject(response.getResponseBody(), FileAssign.class);
            url = String.format("http://%s/%s", fileAssign.getUrl(), fileAssign.getFid());
            if (!dfsConfig.getFileTtl().equals("-1")) {
                url = String.format("%s?ttl=%s", url, dfsConfig.getFileTtl());
            }
            ByteArrayPart byteArrayPart = new ByteArrayPart(fileInfo.getFileName(), fileInfo.getBytes());
            request = new RequestBuilder("POST")
                    .addHeader("Content-type", "multipart/form-data; charset=UTF-8")
                    .setRequestTimeout(requestTimeout).setUrl(url)
                    .addBodyPart(byteArrayPart)
                    .build();
            response = asyncHttpClient.executeRequest(request).get();
            Utility.raiseForStatus(response);
            uploadResult.setOK(true);
            uploadResult.setFid(fileAssign.getFid());
            return uploadResult;
        }
        return uploadResult;
    }

    @Override
    public byte[] get(String fid) throws ExecutionException, InterruptedException, HttpException {
        String url = String.format("http://%s/%s", dfsConfig.getVolumnAddr(), fid);
        Request request = new RequestBuilder("GET")
                .setUrl(url)
                .build();
        Response response = asyncHttpClient.executeRequest(request).get();
        Utility.raiseForStatus(response);
        return response.getResponseBodyAsBytes();
    }
}
