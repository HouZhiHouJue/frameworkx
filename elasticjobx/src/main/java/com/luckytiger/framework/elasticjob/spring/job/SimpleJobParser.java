package com.luckytiger.framework.elasticjob.spring.job;

import com.luckytiger.framework.elasticjob.SimpleJobInfo;
import com.luckytiger.framework.elasticjob.internals.JobProcessor;
import com.luckytiger.framework.elasticjob.internals.JobSchedulers;
import com.luckytiger.framework.elasticjob.internals.cst.JobConst;
import com.luckytiger.framework.elasticjob.spring.BeanRegistrationUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class SimpleJobParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return JobProcessor.class;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String jobName = element.getAttribute(ID_ATTRIBUTE);
        String jobBeanName = element.getAttribute(JobConst.JOB_REF_ATTRIBUTE);
        String cron = element.getAttribute(JobConst.CRON_ATTRIBUTE);
        SimpleJobInfo simpleJobInfo = new SimpleJobInfo(jobName, jobBeanName, cron);
        simpleJobInfo.setJobClass(parserContext.getRegistry().getBeanDefinition(element.getAttribute(JobConst.JOB_REF_ATTRIBUTE)).getBeanClassName());
        simpleJobInfo.setShardingCount(Integer.parseInt(element.getAttribute(JobConst.SHARDING_TOTAL_COUNT_ATTRIBUTE)));
        simpleJobInfo.setShardingItemParameters(element.getAttribute(JobConst.SHARDING_ITEM_PARAMETERS_ATTRIBUTE));
        simpleJobInfo.setJobParameters(element.getAttribute(JobConst.JOB_PARAMETER_ATTRIBUTE));
        simpleJobInfo.setFailover(Boolean.parseBoolean(element.getAttribute(JobConst.FAILOVER_ATTRIBUTE)));
        simpleJobInfo.setMisfire(Boolean.parseBoolean(element.getAttribute(JobConst.MISFIRE_ATTRIBUTE)));
        simpleJobInfo.setJobDesc(element.getAttribute(JobConst.DESCRIPTION_ATTRIBUTE));
        simpleJobInfo.setDisabled(Boolean.parseBoolean(element.getAttribute(JobConst.DISABLED_ATTRIBUTE)));
        simpleJobInfo.setMonitorExcution(Boolean.parseBoolean(element.getAttribute(JobConst.MONITOR_EXECUTION_ATTRIBUTE)));
        simpleJobInfo.setParallel(Boolean.parseBoolean(element.getAttribute(JobConst.PARALLEL_ATTRIBUTE)));
        simpleJobInfo.setJobDesc(element.getAttribute(JobConst.DESCRIPTION_ATTRIBUTE));
        simpleJobInfo.setOverride(Boolean.parseBoolean(element.getAttribute(JobConst.OVERWRITE_ATTRIBUTE)));
        JobSchedulers.register(simpleJobInfo);
    }


}
