package com.xing.middleware.framework.elasticjobx.internals;

import java.util.HashMap;

public class JobContainer {
    private static HashMap<String, SimpleJobExcutorImpl> simpleJobExcutors = new HashMap<>();

    public static void register(String jobName, SimpleJobExcutorImpl simpleJobExcutor) {
        simpleJobExcutors.put(jobName, simpleJobExcutor);
    }

    public static SimpleJobExcutorImpl get(String jobName) {
        if (simpleJobExcutors.containsKey(jobName)) {
            return simpleJobExcutors.get(jobName);
        }
        return null;
    }
}
