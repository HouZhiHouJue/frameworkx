package com.xing.middleware.framework.fdfsx.client;

import com.google.common.io.Files;
import junit.framework.TestCase;
import org.csource.common.NameValuePair;
import org.junit.Assert;

import java.io.File;

/**
 * Created by Jecceca on 2017/8/30.
 */
public class FafstdfsxClientTest extends TestCase {
    public void test() throws Exception {
        FdfsxClient fastdfsxClient = new FdfsxClient("fastdfs-client.properties");
        fastdfsxClient.afterPropertiesSet();
        FdfsFileInfo fdfsFileInfo = new FdfsFileInfo();
        fdfsFileInfo.setExtName("jpg");
        fdfsFileInfo.setFileBuffer(Files.toByteArray(new File("E:/1.jpg")));
        NameValuePair[] nameValuePairs = new NameValuePair[4];

        nameValuePairs[0] = new NameValuePair("width", "1024");
        nameValuePairs[1] = new NameValuePair("heigth", "768");
        nameValuePairs[2] = new NameValuePair("bgcolor", "#000000");
        nameValuePairs[3] = new NameValuePair("title", "Untitle");

        fdfsFileInfo.setMetadata(nameValuePairs);
        for (int i = 0; i < 10; i++) {
            UploadResult uploadResult = fastdfsxClient.uploadFile(fdfsFileInfo);
            System.out.print(uploadResult.getGroupName() + "/" + uploadResult.getRemoteFileName() + "\n");
            FdfsFileInfo fdfsFileInfo2 = fastdfsxClient.downloadFile(uploadResult.getGroupName(), uploadResult.getRemoteFileName());
            Assert.assertArrayEquals(fdfsFileInfo.getFileBuffer(), fdfsFileInfo2.getFileBuffer());
        }
    }
}
