package com.xing.middleware.framework.fdfsx.client.pool;

import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;

/**
 * Created by Jecceca on 2017/9/3.
 */
public class StorageClientExt extends StorageClient {
    private boolean valid;

    public StorageClientExt() {
        super();
        this.valid = true;
    }

    public StorageClientExt(TrackerServer trackerServer, StorageServer storageServer) {
        super(trackerServer, storageServer);
        this.valid = true;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        try {
            this.trackerServer.getSocket().sendUrgentData(0xff);
            this.storageServer.getSocket().sendUrgentData(0xff);
            return valid;
        } catch (Throwable e) {
            return false;
        }
    }

    public void close() throws IOException {
        this.trackerServer.close();
        this.storageServer.close();
    }
}
