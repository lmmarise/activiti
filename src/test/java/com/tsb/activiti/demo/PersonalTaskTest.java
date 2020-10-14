package com.tsb.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： tsb
 * @date： 2020/10/9
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public class PersonalTaskTest {

    private ProcessEngine getDefaultProcessEngine() {
        return ProcessEngines.getDefaultProcessEngine();
    }

    private RuntimeService getRuntimeService() {
        return getDefaultProcessEngine().getRuntimeService();
    }

    private RepositoryService getRepositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * 启动实例, 动态设置assignee
     */
    @Test
    public void dySetAssignee() {
        ProcessEngine processEngine = getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> map = new HashMap<>();
        map.put("a0", "张三");
        map.put("a1", "历史");
        map.put("a2", "cxk");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayProcess", map);

        System.out.println(processEngine.getName());
    }

    /*=====================================================完整流程======================================================*/

    /**
     * 1.部署bpmn流程
     */
    @Test
    void deployProcess() {
        ProcessEngine processEngine = getDefaultProcessEngine();
        RepositoryService repositoryService = getRepositoryService(processEngine);
        // 部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagram\\holiday1.bpmn")
                .addClasspathResource("diagram\\holiday1.png")
                .name("请假流程(变量流程)")
                .deploy();
        // 打印信息
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
