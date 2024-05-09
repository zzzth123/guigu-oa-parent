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
public class ProcessTestGateway {


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
        Map<String,Object> map = new HashMap<>();
        //设置请假天数
        map.put("day","3");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia002",map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }




    //1 部署流程定义
    @Test
    public void deployProcess(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia003.bpmn20.xml")
                .name("加班申请流程003")
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

    //查询某个人的代办任务
    @Test
    public void findTaskList(){
//        String assign = "gouwa";
//        String assign = "wangwu";
        String assign = "xiaoli";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task:list){
            System.out.println("流程实例id: "+task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //处理当前任务
    @Test
    public void completeTask(){
        //查询负责人需要处理任务,返回一条
        Task task = taskService.createTaskQuery()
                .taskAssignee("gouwa").singleResult();
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia003");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }




}
