package kim.kin.service.impl;

/*import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author choky
 */
@Service
public class FlowTestService {
    private static final Logger logger = LoggerFactory.getLogger(FlowTestService.class);
/*    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final ManagementService managementService;
    *//**
     * ACT_ID_GROUP 用户组
     * ACT_ID_USER  用户表
     * ACT_ID_MEMBERSHIP    用户、组关系表
     * ACT_ID_PRIV  权限表
     * ACT_ID_PRIV_MAPPING  用户权限关系表
     *//*
    private final IdentityService identityService;
    private final HistoryService historyService;
    private final FormService formService;
    private final DynamicBpmnService dynamicBpmnService;

    public FlowTestService(RuntimeService runtimeService, RepositoryService repositoryService, TaskService taskService, ManagementService managementService, IdentityService identityService, HistoryService historyService, FormService formService, DynamicBpmnService dynamicBpmnService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.managementService = managementService;
        this.identityService = identityService;
        this.historyService = historyService;
        this.formService = formService;
        this.dynamicBpmnService = dynamicBpmnService;
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public void testFlow() {

        // 开启流程
        Map<String, Object> variables = new HashMap<String, Object>(5);
        variables.put("employee", "admin");
        variables.put("upUser", "upUser");
        variables.put("nrOfHolidays", "Monday ,Tuesday");
        variables.put("description", "go home");
        Authentication.setAuthenticatedUserId("admin");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TestBpmnKey", variables);
        String processInstanceId = processInstance.getId();

//        runtimeService.setProcessInstanceName(processInstanceId,"setInstanceName");

        // 使用businessKey查看任务
//        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey("businessKey").list();

        // 通过名称查询实例
//        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceName("setInstanceName").list();

        // 获取 businessKey
//        String businessKey = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getBusinessKey();

//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        String startUserId = processInstance.getStartUserId();
        logger.info("startUserId :" + startUserId);
        // 流程列表
        // List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("upUser").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        // 获取流程
        Task task = tasks.get(0);
        String taskId = task.getId();
        Map<String, Object> processVariables = taskService.getVariables(taskId);
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        variables = new HashMap<String, Object>(5);
        //同意流程
        variables.put("approved", true);
        taskService.addComment(taskId, processInstance.getProcessInstanceId(), "I agree " + LocalDateTime.now());
        taskService.complete(taskId, variables);
    }

    *//**
     * set and get variables
     *//*
    public void variables() {
        String businessKey = "businessKey";
        String executionId = "executionId";
        String taskId = "taskId";
        String processDefinitionKey = "processDefinitionKey";
        HashMap<String, Object> variables = new HashMap<>(5);
        HashMap<String, Object> transientVariables = new HashMap<>(5);
        // Set variables when the process is started
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // Set variables when task complete
        taskService.complete(taskId, variables, transientVariables);
        // Set variables in task
        taskService.setVariables(taskId, variables);
        taskService.setVariablesLocal(taskId, transientVariables);

        // Get variables
        Map<String, Object> execMap = runtimeService.getVariables(executionId);
        Map<String, Object> execLocalMap = runtimeService.getVariablesLocal(executionId);
        Map<String, Object> map = taskService.getVariables(taskId);
        Map<String, Object> localMap = taskService.getVariablesLocal(taskId);
    }*/

}
