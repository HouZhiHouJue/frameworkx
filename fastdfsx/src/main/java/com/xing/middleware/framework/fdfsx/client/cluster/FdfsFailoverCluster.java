package com.xing.middleware.framework.fdfsx.client.cluster;

import com.xing.middleware.framework.fdfsx.client.FdfsFileInfo;
import com.xing.middleware.framework.fdfsx.client.UploadResult;
import com.xing.middleware.framework.fdfsx.client.pool.StorageClientExt;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.common.MyException;

import java.io.IOException;

/**
 * Created by Jecceca on 2017/9/3.
 */
public class FdfsFailoverCluster {
    GenericObjectPool<StorageClientExt> objectPool;
    int maxTry;

    public FdfsFailoverCluster(GenericObjectPool<StorageClientExt> objectPool, int maxRetry) {
        this.objectPool = objectPool;
        this.maxTry = maxRetry + 1;
    }

    public UploadResult uploadFile(FdfsFileInfo uploadFileInfo) throws Exception {
        for (int i = 0; i < maxTry; i++) {
            Exception ex;
            StorageClientExt storageClientExt = null;
            try {
                storageClientExt = objectPool.borrowObject(1000);
                return FdfsInvoker.uploadFile(storageClientExt, uploadFileInfo);
            } catch (IOException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (MyException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (Exception e) {
                ex = e;
            } finally {
                if (storageClientExt != null)
                    objectPool.returnObject(storageClientExt);
            }
            if (ex != null && i == maxTry - 1)
                throw ex;
        }
        return new UploadResult();
    }

    public void deleleFile(String groupName, String remoteFileName) throws Exception {
        for (int i = 0; i < maxTry; i++) {
            Exception ex;
            StorageClientExt storageClientExt = null;
            try {
                storageClientExt = objectPool.borrowObject(1000);
                FdfsInvoker.deleleFile(storageClientExt, groupName, remoteFileName);
                break;
            } catch (IOException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (MyException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (Exception e) {
                ex = e;
            } finally {
                if (storageClientExt != null)
                    objectPool.returnObject(storageClientExt);
            }
            if (ex != null && i == maxTry - 1)
                throw ex;
        }
    }

    public FdfsFileInfo downloadFile(String groupName, String remoteFileName) throws Exception {
        for (int i = 0; i < maxTry; i++) {
            Exception ex;
            StorageClientExt storageClientExt = null;
            try {
                storageClientExt = objectPool.borrowObject(1000);
                return FdfsInvoker.downloadFile(storageClientExt, groupName, remoteFileName);
            } catch (IOException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (MyException e) {
                storageClientExt.setValid(false);
                ex = e;
            } catch (Exception e) {
                ex = e;
            } finally {
                if (storageClientExt != null)
                    objectPool.returnObject(storageClientExt);
            }
            if (ex != null && i == maxTry - 1)
                throw ex;
        }
        return null;
    }

    public void close() {
        this.objectPool.close();
    }
}
