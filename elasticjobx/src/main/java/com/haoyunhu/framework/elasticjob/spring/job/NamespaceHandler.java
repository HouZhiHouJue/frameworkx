package com.haoyunhu.framework.elasticjob.spring.job;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler  extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("simple", new SimpleJobParser());
    }
}


