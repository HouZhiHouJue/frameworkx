package com.luckytiger.framework.elasticjob.internals;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.google.common.base.Optional;
import org.springframework.beans.factory.InitializingBean;

public final class SpringJobScheduler extends JobScheduler {

    private final ElasticJob elasticJob;

    public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig) {
        super(regCenter, jobConfig);
        this.elasticJob = elasticJob;
    }

    public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig,
                              final JobEventConfiguration jobEventConfig) {
        super(regCenter, jobConfig, jobEventConfig);
        this.elasticJob = elasticJob;
    }


    @Override
    protected Optional<ElasticJob> createElasticJobInstance() {
        return Optional.fromNullable(elasticJob);
    }
}
