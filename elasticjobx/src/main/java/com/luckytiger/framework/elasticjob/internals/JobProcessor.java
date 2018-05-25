package com.luckytiger.framework.elasticjob.internals;

import com.luckytiger.framework.elasticjob.spring.BeanRegistrationUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class JobProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(beanDefinitionRegistry,JobSchedulers.class.getName()
                ,JobSchedulers.class);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
