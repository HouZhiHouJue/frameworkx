package com.xing.middleware.framework.fdfsx.client;

import com.xing.middleware.framework.fdfsx.client.cluster.FdfsFailoverCluster;
import com.xing.middleware.framework.fdfsx.client.pool.PoolConfigBuilder;
import com.xing.middleware.framework.fdfsx.client.pool.StorageClientExt;
import com.xing.middleware.framework.fdfsx.client.pool.StorageClientFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by Jecceca on 2017/8/30.
 */
@Component
public class FdfsxClient implements InitializingBean, DisposableBean {
    private String propsFilePath;
    private int maxRetry;
    FdfsFailoverCluster failoverCluster;

    public FdfsxClient(String propsFilePath, int maxRetry) {
        this.propsFilePath = propsFilePath;
        this.maxRetry = maxRetry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientGlobal.initByProperties(propsFilePath);
        TrackerClient trackerClient = new TrackerClient();
        StorageClientFactory storageClientFactory = new StorageClientFactory(trackerClient);
        GenericObjectPool<StorageClientExt> genericObjectPool = new GenericObjectPool<StorageClientExt>(storageClientFactory, PoolConfigBuilder.buildDefault());
        failoverCluster = new FdfsFailoverCluster(genericObjectPool, maxRetry);
    }

    public UploadResult uploadFile(FdfsFileInfo uploadFileInfo) throws Exception {
        return this.failoverCluster.uploadFile(uploadFileInfo);
    }

    public void deleleFile(String groupName, String remoteFileName) throws Exception {
        failoverCluster.deleleFile(groupName, remoteFileName);
    }

    public FdfsFileInfo downloadFile(String groupName, String remoteFileName) throws Exception {
        return failoverCluster.downloadFile(groupName, remoteFileName);
    }

    @Override
    public void destroy() throws Exception {
        failoverCluster.close();
    }
}
