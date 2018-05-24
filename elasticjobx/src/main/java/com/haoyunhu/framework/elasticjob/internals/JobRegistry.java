package com.haoyunhu.framework.elasticjob.internals;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.haoyunhu.framework.elasticjob.SimpleJobInfo;
import com.haoyunhu.framework.elasticjob.internals.cst.ConfigConst;
import com.haoyunhu.framework.elasticjob.internals.cst.RegistryConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JobRegistry implements DisposableBean, InitializingBean {
    private CoordinatorRegistryCenter regCenter;
    private static final HashMap<String, SimpleJobInfo> jobConfigurations = new HashMap<>();
    private final HashMap<String, JobScheduler> jobSchedulers = new HashMap<>();


    public static void register(SimpleJobInfo simpleJobInfo) {
        jobConfigurations.put(simpleJobInfo.getJobName(), simpleJobInfo);
    }

    public static SimpleJobInfo get(String jobName) {
        if (jobConfigurations.containsKey(jobName)) {
            return jobConfigurations.get(jobName);
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        jobConfigurations.clear();
        for (Map.Entry<String, JobScheduler> entry : jobSchedulers.entrySet()) {
            entry.getValue().getSchedulerFacade().shutdownInstance();
        }
        jobSchedulers.clear();
        this.regCenter.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = ConfigService.getConfig(ConfigConst.ELASTICJOB_PUBLIC_NAMESPACE);
        String value = config.getProperty(ConfigConst.ELASTICJOB_REGISTRY_ADDR_KEY, RegistryConst.ELASTICJOB_REGISTRY_DEFAUTL_ADDR);
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(value, RegistryConst.DEFAULT_JOB_NAMESPANCE);
        regCenter = new ZookeeperRegistryCenter(zkConfig);
        regCenter.init();

        for (Map.Entry<String, SimpleJobInfo> entry : jobConfigurations.entrySet()) {
            SimpleJobInfo simpleJobInfo = entry.getValue();
            if (!org.quartz.CronExpression.isValidExpression(simpleJobInfo.getCron())) {
                log.error(String.format("%s init error,invalid cron expression", simpleJobInfo.getJobName()));
                return;
            }
            JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(simpleJobInfo.getJobName(), simpleJobInfo.getCron(),
                    simpleJobInfo.getShardingCount()).shardingItemParameters(simpleJobInfo.getShardingItemParameters())
                    .description(simpleJobInfo.getJobDesc()).failover(simpleJobInfo.isFailover()).misfire(simpleJobInfo.isMisfire())
                    .jobParameter(simpleJobInfo.getJobParameters()).build();
            SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(coreConfig, simpleJobInfo.getJobClass());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfig)
                    .disabled(simpleJobInfo.isDisabled()).overwrite(simpleJobInfo.isOverride()).monitorExecution(simpleJobInfo.isMonitorExcution()).build();
            JobScheduler jobScheduler = new JobScheduler(regCenter, liteJobConfiguration);
            jobScheduler.init();
            jobSchedulers.put(simpleJobInfo.getJobName(), jobScheduler);
        }
    }
}
