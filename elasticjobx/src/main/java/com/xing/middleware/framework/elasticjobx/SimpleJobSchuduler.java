package com.xing.middleware.framework.elasticjobx;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.xing.middleware.framework.elasticjobx.internals.Consts;
import com.xing.middleware.framework.elasticjobx.internals.JobContainer;
import com.xing.middleware.framework.elasticjobx.internals.SimpleJobExcutor;
import com.xing.middleware.framework.elasticjobx.internals.SimpleJobExcutorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleJobSchuduler implements InitializingBean, DisposableBean {

    private List<SimpleJobInfo> simpleJobs;
    private HashMap<String, JobScheduler> jobSchedulerHashMap = new HashMap<>();
    private CoordinatorRegistryCenter regCenter;

    public SimpleJobSchuduler(List<SimpleJobInfo> simpleJobs) {
        this.simpleJobs = simpleJobs;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.regCenter = setUpRegistryCenter();
        for (SimpleJobInfo simpleJobInfo : simpleJobs) {
            if (!org.quartz.CronExpression.isValidExpression(simpleJobInfo.getCron())) {
                log.error(String.format("%s init error,invalid cron expression", simpleJobInfo.getJobName()));
                continue;
            }
            SimpleJobExcutorImpl simpleJobExcutor = buildSimpleJobExcutor(simpleJobInfo);
            if (simpleJobExcutor == null) {
                continue;
            }
            JobContainer.register(simpleJobInfo.getJobName(), simpleJobExcutor);
            JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(simpleJobInfo.getJobName(), simpleJobInfo.getCron(),
                    simpleJobInfo.getShardingCount()).shardingItemParameters(simpleJobInfo.getShardingParameters())
                    .description(simpleJobInfo.getJobDesc()).failover(simpleJobInfo.isFailover()).misfire(simpleJobInfo.isMisfire())
                    .jobParameter(simpleJobInfo.getJobParameters()).build();
            SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(coreConfig, SimpleJobExcutor.class.getCanonicalName());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfig)
                    .disabled(simpleJobInfo.isDisabled()).overwrite(simpleJobInfo.isOverride()).monitorExecution(simpleJobInfo.isMonitorExcution()).build();
            JobScheduler jobScheduler = new JobScheduler(regCenter, liteJobConfiguration);
            jobScheduler.init();
            jobSchedulerHashMap.put(simpleJobInfo.getJobName(), jobScheduler);
        }
    }

    private CoordinatorRegistryCenter setUpRegistryCenter() {
        Config config = ConfigService.getConfig(Consts.ELASTICJOB_PUBLIC_NAMESPACE);
        String value = config.getProperty(Consts.ELASTICJOB_REGISTRY_ADDR_KEY, Consts.ELASTICJOB_REGISTRY_DEFAUTL_ADDR);
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(value, Consts.DEFAULT_JOB_NAMESPANCE);
        CoordinatorRegistryCenter result = new ZookeeperRegistryCenter(zkConfig);
        result.init();
        return result;
    }

    private SimpleJobExcutorImpl buildSimpleJobExcutor(SimpleJobInfo simpleJobInfo) {
        try {
            SimpleJob simpleJob = (SimpleJob) Class.forName(simpleJobInfo.getJobClass()).newInstance();
            SimpleJobExcutorImpl simpleJobExcutor = new SimpleJobExcutorImpl(simpleJob, simpleJobInfo.isParallel());
            return simpleJobExcutor;
        } catch (Throwable e) {
            log.error(String.format("%s init error,ignored", simpleJobInfo.getJobName()), e);
        }
        return null;
    }


    @Override
    public void destroy() throws Exception {
        for (Map.Entry<String, JobScheduler> entry : jobSchedulerHashMap.entrySet()) {
            entry.getValue().getSchedulerFacade().shutdownInstance();
        }
        jobSchedulerHashMap.clear();
        this.regCenter.close();
    }

}
