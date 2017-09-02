package com.xing.middleware.framework.fdfsx.client;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Jecceca on 2017/8/30.
 */
@Component
public class FdfsxClient implements InitializingBean, DisposableBean {
    private String propsFilePath;
    private TrackerClient trackerClient;
    private StorageClient uploadClient;
    private StorageClient downloadClient;

    public FdfsxClient(String propsFilePath) {
        this.propsFilePath = propsFilePath;
    }

    public UploadResult uploadFile(FdfsFileInfo uploadFileInfo) throws IOException, MyException {
        UploadResult uploadResult = new UploadResult();
        String[] result = uploadClient.upload_file(uploadFileInfo.getFileBuffer(), uploadFileInfo.getExtName(), uploadFileInfo.getMetadata());
        if (result != null) {
            uploadResult.setOk(true);
            uploadResult.setGroupName(result[0]);
            uploadResult.setRemoteFileName(result[1]);
        }
        return uploadResult;
    }

    public void deleleFile(String groupName, String remoteFileName) throws IOException, MyException {
        downloadClient.delete_file(groupName, remoteFileName);
    }

    public FdfsFileInfo downloadFile(String groupName, String remoteFileName) throws IOException, MyException {
        FdfsFileInfo fdfsFileInfo = new FdfsFileInfo();
        fdfsFileInfo.setFileBuffer(downloadClient.download_file(groupName, remoteFileName));
        NameValuePair[] nameValuePairs = downloadClient.get_metadata(groupName, remoteFileName);
        fdfsFileInfo.setMetadata(nameValuePairs);
        return fdfsFileInfo;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientGlobal.initByProperties(propsFilePath);
        trackerClient = new TrackerClient();
        uploadClient = new StorageClient(trackerClient.getConnection(), null);
        downloadClient = new StorageClient(trackerClient.getConnection(), null);
    }
}
