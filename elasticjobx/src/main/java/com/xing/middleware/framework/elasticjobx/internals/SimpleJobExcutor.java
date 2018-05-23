package com.xing.middleware.framework.elasticjobx.internals;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class SimpleJobExcutor implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        SimpleJobExcutorImpl simpleJobExcutor = JobContainer.get(shardingContext.getJobName());
        if (simpleJobExcutor != null) {
            simpleJobExcutor.execute(shardingContext);
        }
    }
}
