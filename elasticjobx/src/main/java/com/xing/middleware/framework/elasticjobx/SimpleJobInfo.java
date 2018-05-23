package com.xing.middleware.framework.elasticjobx;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SimpleJobInfo {
    @NonNull
    private String jobName;
    @NonNull
    private String jobClass;
    @NonNull
    private String cron;

    private String jobDesc;

    private String jobParameters;

    private boolean disabled;

    private boolean misfire = true;

    private boolean parallel = false;

    private boolean override = true;

    private boolean monitorExcution = false;

    private boolean failover = false;

    private int shardingCount = 1;

    private String shardingParameters = "0=default";
}
