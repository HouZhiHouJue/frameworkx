package com.luckytiger.framework.dfs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadResult {
    private byte[] bytes;
    private boolean isOK;
}
