package com.luckytiger.framework.dfs.internals;

import com.ctrip.framework.apollo.Config;
import com.luckytiger.framework.dfs.internals.cst.ConfigConst;
import lombok.Synchronized;

public class DfsConfigParser {

    private static DfsConfig dfsConfig;

    @Synchronized
    public static void init(Config config) {
        if (dfsConfig == null) {
            dfsConfig = new DfsConfig();
            dfsConfig.setConnectionTimeout(config.getIntProperty(ConfigConst.DFS_CONNECTION_TIMEOUT_KEY, dfsConfig.getConnectionTimeout()));
            dfsConfig.setConnectionTtl(config.getIntProperty(ConfigConst.DFS_CONNECTION_TTL_KEY, dfsConfig.getConnectionTtl()));
            dfsConfig.setMaxConnections(config.getIntProperty(ConfigConst.DFS_MAX_CONNECTIONS_KEY, dfsConfig.getMaxConnections()));
            dfsConfig.setMaxConnectionsPerHost(config.getIntProperty(ConfigConst.DFS_MAX_CONNECTIONS_PER_HOST_KEY, dfsConfig.getMaxConnectionsPerHost()));
            dfsConfig.setReadTimeout(config.getIntProperty(ConfigConst.DFS_READ_TIMEOUT_KEY, dfsConfig.getReadTimeout()));
            dfsConfig.setRequestTimeout(config.getIntProperty(ConfigConst.DFS_REQUEST_TIMEOUT_KEY, dfsConfig.getRequestTimeout()));
            dfsConfig.setMasterAddr(config.getProperty(ConfigConst.DFS_MASTER_ADDR_KEY, ConfigConst.DFS_MASTER_DEFAULT_ADDR));
            dfsConfig.setFileTtl(config.getProperty(ConfigConst.DFS_FILE_TTL_KEY, ConfigConst.DFS_FILE_DEFAULT_TTL));
            dfsConfig.setVolumnAddr(config.getProperty(ConfigConst.DFS_VOLUMN_ADDR_KEY, ConfigConst.DFS_VOLUMN_DEFAULT_ADDR));
        }
    }

    public static DfsConfig getDfsConfig() {
        return dfsConfig;
    }

}
