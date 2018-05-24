package com.haoyunhu.framework.elasticjob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class JobContext {
    /**
     * 作业名称.
     */
    private final String jobName;

    /**
     * 作业任务ID.
     */
    private final String taskId;

    /**
     * 分片总数.
     */
    private final int shardingTotalCount;

    /**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
    private final String jobParameter;

    /**
     * 分配于本作业实例的分片项.
     */
    private final int shardingItem;

    /**
     * 分配于本作业实例的分片参数.
     */
    private final String shardingParameter;

}
