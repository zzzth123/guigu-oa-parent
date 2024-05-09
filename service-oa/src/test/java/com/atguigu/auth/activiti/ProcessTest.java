package com.atguigu.auth.activiti;


import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessTest {

    //注入RepositoryService
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;






    //把单个流程实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "bcfd3a87-fcbb-11ee-ab66-be0358f6f9b4";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        boolean suspended = processInstance.isSuspended();
        if (suspended){
            //激活
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println(processInstanceId+"激活");
        }else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println(processInstanceId+"挂起");
        }

    }

    //全部流程实例挂起
    @Test
    public void suspendProcessInstanceAll(){
        //获取流程定义的对象
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("qingjia").singleResult();
        //调用流程定义对象里面的方法，判断当前状态:挂起、激活
        boolean suspended = qingjia.isSuspended();
        //如果是挂起，实现激活
        if (suspended){
            //第一个参数 流程定义id
            //第二个参数 是否激活
            //第三个参数 时间点
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId()+"激活了");
        }else {
            //如果是激活，实现挂起
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId()+"挂起");
        }


    }


    //创建流程实例，指定BusinessKey
    @Test
    public void startUpProcessAddBusinessKey(){
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        System.out.println(instance.getBusinessKey());
        System.out.println(instance.getId());
    }

    //查询已经处理的任务
    @Test
    public void findcompleteTaskList(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("lisi")
                .finished().list();
        for (HistoricTaskInstance historicTaskInstance:list){
            System.out.println("实例id: "+historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    //处理当前任务
    @Test
    public void completeTask(){
        //查询负责人需要处理任务,返回一条
        Task task = taskService.createTaskQuery()
                .taskAssignee("zhangsan").singleResult();
        //完成任务,参数是任务id
        taskService.complete(task.getId());
    }

    //查询某个人的代办任务
    @Test
    public void findTaskList(){
        String assign = "lucy02";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task:list){
            System.out.println("流程实例id: "+task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }


    //启动流程的实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println("流程定义id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id: "+ processInstance.getId());
        System.out.println("活动id: "+ processInstance.getActivityId());
    }


    //单个文件的部署
    @Test
    public void deployProcess(){
        //流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
