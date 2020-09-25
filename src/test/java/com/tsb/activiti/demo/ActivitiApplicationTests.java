package com.tsb.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;


@SpringBootTest
class ActivitiApplicationTests {

    private final static Logger log = LoggerFactory.getLogger(ActivitiApplicationTests.class);

    @Test
    void contextLoads() {
    }

    @Test
    void testActiviti() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }

    /*================================================创建流程部署流程完成流程=============================================*/
    /**
     * 流程定义部署
     */
    @Test
    void deploymentFlowchart() {
        // 1. 创建引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获得服务
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram/holiday.bpmn")
                .name("请假申请流程")
                .deploy();
        // 4. 输出部署信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());
    }

    /**
     * 完成流程部署之后, 启动流程
     */
    @Test
    void startFlowchart() {
        // 1. 获得引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获得服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3. 创建流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayProcess");
        // 4. 输出流程相关信息
        log.info("流程部署id: {}", processInstance.getDeploymentId());
        log.info("流程定义id: {}", processInstance.getProcessDefinitionId());
        log.info("流程实例id: {}", processInstance.getId());
        log.info("流程活动id: {}", processInstance.getActivityId());
    }

    /**
     * 查询用户当前的任务
     */
    @Test
    void findUserTask() {
        // 1. 获得引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获得服务
        TaskService taskService = processEngine.getTaskService();
        // 3. 获得指定用户的任务列表
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("holidayProcess")
                .taskAssignee("cxk")
                .list();
        // 4. 展示任务
        for (Task task : taskList) {
            log.info("流程实例id: {}", task.getProcessInstanceId());
            log.info("任务id: {}", task.getId());
            log.info("任务负责人: {}", task.getAssignee());
            log.info("任务名称: {}", task.getName());
        }
    }

    /**
     * 处理任务
     * act_hi_actinst
     * act_hi_identitylink
     * act_hi_taskinst
     * act_ru_identitylink
     * act_ru_task
     */
    @Test
    void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("2505");
    }

    /**
     * 查询当前用户任务并处理
     */
    @Test
    void queryTaskAndComplete() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayProcess")
                .taskAssignee("cxk")
                .singleResult();
        taskService.complete(task.getId());
        log.info("完成任务id: {}", task.getId());
    }

    /*==================================================单文件部署======================================================*/

    /**
     * 通过zip压缩文件进行部署
     */
    @Test
    void singleFileDeployment() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("diagram/holidayBPMN.zip");
        if (is == null) throw new RuntimeException("读取文件失败");
        ZipInputStream zis = new ZipInputStream(is);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zis)
                .name("请假申请流程")
                .deploy();
        log.info("请假流程id: {}", deployment.getId());
        log.info("请假流程名称: {}", deployment.getId());
    }

    /*===================================================查询条件=======================================================*/

    /**
     * 根据查询条件查询
     */
    @Test
    void queryByQueryDefinition() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.processDefinitionKey("holidayProcess")
                .orderByProcessDefinitionVersion()
                .desc().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            log.info("流程定义ID: {}", processDefinition.getId());
            log.info("流程定义名称: {}", processDefinition.getName());
            log.info("流程定义的Key: {}", processDefinition.getKey());
            log.info("流程定义的Version: {}", processDefinition.getVersion());
        }
    }

    /*===================================================删除流程=======================================================*/

    /**
     * 删除流程定义信息
     * true 级联删除
     * false 代表不级联
     */
    @Test
    void delete() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("1", true);
    }

    /*===================================================流程输出=======================================================*/

    /**
     * 存储到指定的某个目录下文件的保存
     */
    @Test
    void queryBpmnFile() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 查询条件
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery.processDefinitionKey("holidayProcess");
        // 查出
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        String deploymentId = processDefinition.getDeploymentId();
        // 获取指定资源
        String pngName = processDefinition.getDiagramResourceName();
        String bpmnName = processDefinition.getResourceName();
        InputStream pngIs = repositoryService.getResourceAsStream(deploymentId, pngName);
        InputStream diagramIs = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 输出到磁盘目录
        try (
                FileOutputStream pngFos = new FileOutputStream(this.getClass().getResource("/diagram-out") + pngName);
                FileOutputStream bpmnFos = new FileOutputStream(this.getClass().getResource("/diagram-out") + bpmnName);
        ) {

        } catch (Exception e) {

        }
    }
}
