package com.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiActivitiTest {
    private final Logger logger = LoggerFactory.getLogger(ActivitiTest.class);
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * 启动流程
     */
    @Test
    public void start() throws InterruptedException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("courseCode", "1502");
        String businessKey="assessment";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("answerToAssessmentNojudge",businessKey, variables);
        logger.info(">>>>>>>>>>>>>>>>>>>>启动流程：" + processInstance.getId());
        String assignee = "huohuohuo";
        List<Task> list = taskService
                .createTaskQuery()//
                .taskAssignee(assignee)//个人任务的查询
                .list();
        list.forEach(task -> {
            String taskId = task.getId();
            logger.info(">>>>>>>>>>>>>>>>>>>>任务D：" + taskId);
            logger.info(">>>>>>>>>>>>>>>>>>>>任务的办理人：" + task.getAssignee());
            logger.info(">>>>>>>>>>>>>>>>>>>>传递参数：" + taskService.getVariable(taskId,"timeout1"));
            logger.info(">>>>>>>>>>>>>>>>>>>>任务名称：" + task.getName());
            logger.info(">>>>>>>>>>>>>>>>>>>>任务的创建时间：" + task.getCreateTime());
            logger.info(">>>>>>>>>>>>>>>>>>>>流程实例ID：" + task.getProcessInstanceId());
            logger.info(">>>>>>>>>>>>>>>>>>>>#######################################");
            taskService.complete(taskId);
            logger.info(">>>>>>>>>>>>>>>>>>>>完成任务：" + taskId);
            logger.info(">>>>>>>>>>>>>>>>>>>>#######################################");
        });
        if (taskService.createTaskQuery().taskAssignee(assignee).list() == null || taskService.createTaskQuery().taskAssignee(assignee).list().size() == 0) {
            logger.info(">>>>>>>>>>>>>>>>>>>>我已经没有任务了");
        }
        Thread.sleep(15000);
    }

}
