package com.xing.middleware.framework.fdfsx.client;

/**
 * Created by Jecceca on 2017/8/30.
 */
public class UploadResult {
    private boolean isOk;
    private String groupName;
    private String remoteFileName;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }
}
