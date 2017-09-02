package com.xing.middleware.framework.fdfsx.client;

import org.csource.common.NameValuePair;

/**
 * Created by Jecceca on 2017/8/30.
 */
public class FdfsFileInfo {
    private byte[] fileBuffer;
    private String extName;
    private NameValuePair[] metadata;

    public FdfsFileInfo() {
        extName = "pdf";
    }

    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public NameValuePair[] getMetadata() {
        return metadata;
    }

    public void setMetadata(NameValuePair[] metadata) {
        this.metadata = metadata;
    }
}
