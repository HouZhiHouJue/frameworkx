package com.xing.demo.bo;

import com.haoyunhu.framework.elasticjob.JobContext;
import com.haoyunhu.framework.elasticjob.SimpleJobExcutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJob2 extends SimpleJobExcutor {
    @Override
    public void run(JobContext jobContext) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                jobContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));
    }
}
