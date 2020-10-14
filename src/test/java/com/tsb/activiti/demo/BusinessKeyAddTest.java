package com.tsb.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

/**
 * @author： tsb
 * @date： 2020/10/9
 * @description：启动实例流程添加BusinessKey
 * @modifiedBy：
 * @version: 1.0
 */
public class BusinessKeyAddTest {

    @Test
    void start() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程key 业务标识businessKey(请假单id)
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayProcess", "1001");
        System.out.println(processInstance.getBusinessKey());
    }

    /**
     * 全部流程实例挂起
     */
    @Test
    void suspendAll() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("holidayProcess")
                .singleResult();
        String processDefinitionId = processDefinition.getId();
        boolean suspended = processDefinition.isSuspended();
        if (suspended) {
            // 将暂停状态变更为激活
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义:" + processDefinitionId + "已激活");
        } else {
            // 暂停
            repositoryService.suspendProcessDefinitionById(processDefinitionId);
            System.out.println("流程定义:" + processDefinitionId + "已暂停");
        }
    }

    /**
     * 单个流程实例的挂起
     */
    @Test
    void suspendOne() {
        String processInstanceId = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "已激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "已暂停");
        }
    }

    /**
     * 完成任务
     */
    @Test
    void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayProcess")
                .taskAssignee("cxk")
                .singleResult();
        taskService.complete(task.getId());
        System.out.println("任务完成: " + task.getId());
    }
}
