package com.xing.middleware.framework.disruptor.demo.middleware.framework.fdfsx.client;

import com.google.common.io.Files;
import junit.framework.TestCase;
import org.csource.common.NameValuePair;
import org.junit.Assert;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jecceca on 2017/8/30.
 */
public class FafstdfsxClientTest extends TestCase {
    public void test() throws Exception {
        final FdfsxClient fastdfsxClient = new FdfsxClient("fastdfs-client.properties", 2);
        fastdfsxClient.afterPropertiesSet();
        final FdfsFileInfo fdfsFileInfo = new FdfsFileInfo();
        fdfsFileInfo.setExtName("jpg");
        fdfsFileInfo.setFileBuffer(Files.toByteArray(new File("E:/1.jpg")));
        NameValuePair[] nameValuePairs = new NameValuePair[4];

        nameValuePairs[0] = new NameValuePair("width", "1024");
        nameValuePairs[1] = new NameValuePair("heigth", "768");
        nameValuePairs[2] = new NameValuePair("bgcolor", "#000000");
        nameValuePairs[3] = new NameValuePair("title", "Untitle");

        fdfsFileInfo.setMetadata(nameValuePairs);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    UploadResult uploadResult = null;
                    try {
                        uploadResult = fastdfsxClient.uploadFile(fdfsFileInfo);
                        System.out.print(uploadResult.getGroupName() + "/" + uploadResult.getRemoteFileName() + "\n");
                        FdfsFileInfo fdfsFileInfo2 = fastdfsxClient.downloadFile(uploadResult.getGroupName(), uploadResult.getRemoteFileName());
                        Assert.assertArrayEquals(fdfsFileInfo.getFileBuffer(), fdfsFileInfo2.getFileBuffer());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread.sleep(100 * 1000);
        threadPoolExecutor.shutdown();
        fastdfsxClient.destroy();
    }
}
