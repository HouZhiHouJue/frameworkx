package com.xing.middleware.framework.elasticjobx.internals;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class SimpleJobExcutorImpl {
    @NonNull
    private SimpleJob simpleJob;
    @NonNull
    private boolean parallel;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public void execute(ShardingContext shardingContext) {
        if (parallel || !isRunning.get()) {
            excuteImpl(shardingContext);
        } else {
            log.warn(String.format("%s is running,skip this scheduler", shardingContext.getJobName()));
        }
    }

    private void excuteImpl(ShardingContext shardingContext) {
        try {
            isRunning.compareAndSet(false, true);
            simpleJob.execute(shardingContext);
            log.info(String.format("Job Execute ok,jobName:%s,parameters:%s,shardingItemParameters:%s", shardingContext.getJobName(),
                    shardingContext.getJobParameter(), JSON.toJSONString(shardingContext.getShardingParameter())));
        } catch (Throwable e) {
            log.error(String.format("Job Execute error,jobName:%s,parameters:%s,shardingItemParameters:%s", shardingContext.getJobName(),
                    shardingContext.getJobParameter(), JSON.toJSONString(shardingContext.getShardingParameter())), e);
        } finally {
            isRunning.compareAndSet(true, false);
        }
    }
}
