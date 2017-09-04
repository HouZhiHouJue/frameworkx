package com.xing.middleware.framework.fdfsx.client.cluster;

import com.xing.middleware.framework.fdfsx.client.FdfsFileInfo;
import com.xing.middleware.framework.fdfsx.client.UploadResult;

/**
 * Created by Jecceca on 2017/9/3.
 */
public interface FdfsCluster {
    UploadResult uploadFile(FdfsFileInfo uploadFileInfo) throws Exception;

    void deleleFile(String groupName, String remoteFileName) throws Exception;

    FdfsFileInfo downloadFile(String groupName, String remoteFileName) throws Exception;

    void close();
}
