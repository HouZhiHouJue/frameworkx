package com.xing.middleware.framework.fdfsx.client.cluster;

import com.xing.middleware.framework.fdfsx.client.FdfsFileInfo;
import com.xing.middleware.framework.fdfsx.client.UploadResult;
import com.xing.middleware.framework.fdfsx.client.pool.StorageClientExt;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;

import java.io.IOException;

/**
 * Created by Jecceca on 2017/9/3.
 */
public class FdfsInvoker {
    public static UploadResult uploadFile(StorageClientExt storageClientExt, FdfsFileInfo uploadFileInfo) throws IOException, MyException {
        UploadResult uploadResult = new UploadResult();
        String[] result = storageClientExt.upload_file(uploadFileInfo.getFileBuffer(), uploadFileInfo.getExtName(), uploadFileInfo.getMetadata());
        if (result != null) {
            uploadResult.setOk(true);
            uploadResult.setGroupName(result[0]);
            uploadResult.setRemoteFileName(result[1]);
        }
        return uploadResult;
    }

    public static void deleleFile(StorageClientExt storageClientExt, String groupName, String remoteFileName) throws IOException, MyException {
        storageClientExt.delete_file(groupName, remoteFileName);
    }

    public static FdfsFileInfo downloadFile(StorageClientExt storageClientExt, String groupName, String remoteFileName) throws IOException, MyException {
        FdfsFileInfo fdfsFileInfo = new FdfsFileInfo();
        fdfsFileInfo.setFileBuffer(storageClientExt.download_file(groupName, remoteFileName));
        NameValuePair[] nameValuePairs = storageClientExt.get_metadata(groupName, remoteFileName);
        fdfsFileInfo.setMetadata(nameValuePairs);
        return fdfsFileInfo;
    }
}
