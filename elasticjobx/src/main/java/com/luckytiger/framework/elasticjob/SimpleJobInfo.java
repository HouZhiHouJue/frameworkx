package com.luckytiger.framework.elasticjob;

import com.dangdang.ddframe.job.api.ElasticJob;
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
    private String jobBeanName;
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

    private String shardingItemParameters = "0=default";

    private String jobClass;

    private ElasticJob elasticJob;
}
