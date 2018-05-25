package com.luckytiger.framework.elasticjob;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.luckytiger.framework.elasticjob.internals.JobSchedulers;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class SimpleJobExcutor implements SimpleJob {

    public abstract void run(JobContext jobContext);

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    public final void execute(ShardingContext shardingContext) {
        SimpleJobInfo simpleJobInfo = JobSchedulers.get(shardingContext.getJobName());
        if (simpleJobInfo.isParallel() || !isRunning.get()) {
            excuteImpl(shardingContext);
        } else {
            log.warn(String.format("%s is isRunning,skip this scheduler", shardingContext.getJobName()));
        }
    }

    private void excuteImpl(ShardingContext shardingContext) {
        try {
            isRunning.compareAndSet(false, true);
            JobContext jobContext = new JobContext(shardingContext.getJobName(), shardingContext.getTaskId(), shardingContext.getShardingTotalCount(),
                    shardingContext.getJobParameter(), shardingContext.getShardingItem(), shardingContext.getShardingParameter());
            run(jobContext);
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
