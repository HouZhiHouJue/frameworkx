package com.xing.middleware.framework.fdfsx.client.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.StructGroupStat;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.util.Random;

/**
 * Created by Jecceca on 2017/9/3.
 */
public class StorageClientFactory implements PooledObjectFactory<StorageClientExt> {
    private TrackerClient trackerClient;
    private Random random;

    public StorageClientFactory(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public PooledObject<StorageClientExt> makeObject() throws Exception {
        TrackerServer trackerServer = trackerClient.getConnection();
        StructGroupStat[] structGroupStat = trackerClient.listGroups(trackerServer);
        int nextInt = random.nextInt(structGroupStat.length);
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, structGroupStat[nextInt].getGroupName());
        StorageClientExt storageClientExt = new StorageClientExt(trackerServer, storageServer);
        return new DefaultPooledObject(storageClientExt);
    }

    @Override
    public void destroyObject(PooledObject<StorageClientExt> pooledObject) throws Exception {
        StorageClientExt storageClientExt = pooledObject.getObject();
        if (storageClientExt != null) {
            storageClientExt.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<StorageClientExt> pooledObject) {
        StorageClientExt storageClientExt = pooledObject.getObject();
        if (storageClientExt != null) {
            return storageClientExt.isValid();
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<StorageClientExt> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<StorageClientExt> pooledObject) throws Exception {

    }
}
