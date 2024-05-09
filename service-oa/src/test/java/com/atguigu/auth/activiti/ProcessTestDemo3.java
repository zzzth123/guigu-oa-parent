package com.atguigu.auth.activiti;


import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProcessTestDemo3 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    //监听器的方式来分配任务
    @Test
    public void deployProcess02(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban02.bpmn20.xml")
                .name("加班申请流程02")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void startProcessInstance02(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban02");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }



    //uel-method
    @Test
    public void deployProcess01(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban01.bpmn20.xml")
                .name("加班申请流程01")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void startProcessInstance01(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban01");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }




    //1 部署流程定义
    @Test
    public void deployProcess(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban04.bpmn20.xml")
                .name("加班申请流程04")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    //2 查询组任务
    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //3 拾取组任务
    @Test
    public void claimTask(){
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .singleResult();
        if (task!=null){
            taskService.claim(task.getId(),"tom01");
            System.out.println("任务拾取完成");
        }
    }

    //4 查询某个人的代办任务
    @Test
    public void findTaskList(){
        String assign = "tom01";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task:list){
            System.out.println("流程实例id: "+task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //5 办理个人任务
//处理当前任务
    @Test
    public void completeTask(){
        //查询负责人需要处理任务,返回一条
        Task task = taskService.createTaskQuery()
                .taskAssignee("tom01").singleResult();
        //完成任务,参数是任务id
        taskService.complete(task.getId());
    }


    //启动流程实例
    @Test
    public void startProcessInstance(){
        Map<String,Object> map = new HashMap<>();
        //设置任务人
//        map.put("assignee1","lucy02");
//        map.put("assignee2","mary02");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban04", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }





}
