package kim.kin.rest;

import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jsoup.helper.StringUtil;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.conf.RuntimeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author choky
 */
@RestController
@RequestMapping(value = "/jbpm")
@Configuration
public class JbpmControl {
//    @Value("${jbpm.default.processId}")
    private String defaultProcessId="test";

    private final KieSession kieSession;
    private final TaskService taskService;
    private final RuntimeDataService runtimeDataService;
    private final DeploymentService deploymentService;


    @GetMapping(value = "startProcess")
    public ResponseEntity<Object> startProcess1(String userId, String processId) {

        long obj = startProcess(userId, processId);
        return ResponseEntity.ok(obj);
    }
    public JbpmControl(KieSession kieSession, TaskService taskService, RuntimeDataService runtimeDataService, DeploymentService deploymentService) {
        this.kieSession = kieSession;
        this.taskService = taskService;
        this.runtimeDataService = runtimeDataService;
        this.deploymentService = deploymentService;
    }


    public Long startProcess(String userId) {
        Map<String, Object> data = new HashMap<>(16);
        data.put("Manager", userId);

        return kieSession.startProcess(defaultProcessId, data).getId();
    }

    public Long startProcess(String userId, Map<String, Object> data) {
        return kieSession.startProcess(defaultProcessId, data).getId();
    }

    public Long startProcess(String userId, String processId) {
        if (StringUtil.isBlank(processId)) {
            return startProcess(userId);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("Manager", userId);
        return kieSession.startProcess(processId, data).getId();
    }

    public Long startProcess(String userId, String processId, Map<String, Object> data) {
        if (data == null) {
            data = new HashMap<>(16);
        }
        data.put("Manager", userId);

        if (StringUtil.isBlank(processId)) {
            return startProcess(userId, data);
        }

        return kieSession.startProcess(processId, data).getId();
    }


    @PostMapping(value = "startProcessData")
    public ResponseEntity<Object> startProcessData(String userId, String processId, @Valid @RequestBody Map<String, Object> data) {
        long obj = startProcess(userId, processId, data);
        return ResponseEntity.ok(obj);
    }

    @PostMapping(value = "startProcessNew")
    public ResponseEntity<Object> startProcessNewPost(String userId, String processId, @Valid @RequestBody Map<String, Object> data) {

        long processInstanceId = startProcess(userId, processId, data);
        List<TaskSummary> list = taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null, userId);
        System.out.println(list.size());
        Map<String, Object> map = new HashMap<>(5);
        map.put("processInstanceId", processInstanceId);
        map.put("taskList", list);

        return ResponseEntity.ok(map);
    }




    @GetMapping(value = "startProcessNew")
    public ResponseEntity<Object> startProcessNew(String userId, String processId) {
        long processInstanceId = startProcess(userId, processId);
        List<TaskSummary> list = taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null, userId);
        System.out.println(list.size());
        Map<String, Object> map = new HashMap<>(5);
        map.put("processInstanceId", processInstanceId);
        map.put("taskList", list);
        return ResponseEntity.ok(map);
    }


    @GetMapping(value = "getMyAssignedTask")
    public ResponseEntity<Object> getMyAssignedTask(String userId) {
        List<TaskSummary> obj = taskService.getTasksAssignedAsPotentialOwner(userId, "en_US");
        return ResponseEntity.ok(obj);
    }


    @GetMapping(value = "getProcessTaskList")
    public ResponseEntity<Object> getProcessTaskList(Long processInstanceId) {

        List<TaskSummary> obj = taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null, "en_US");


        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "getProcessTaskListByStatus")
    public ResponseEntity<Object> getProcessTaskListByStatus(Long processInstanceId, Status status) {


        List<Status> statusList = new ArrayList<>();
        statusList.add(status);
        List<TaskSummary> obj = taskService.getTasksByStatusByProcessInstanceId(processInstanceId, statusList, "en_US");


        return ResponseEntity.ok(obj);

    }

    @GetMapping(value = "getTaskById")
    public ResponseEntity<Object> getTaskById(Long taskId) {

        Task obj = taskService.getTaskById(taskId);


        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "getMyOwnedTaskNouser")
    public ResponseEntity<Object> getMyOwnedTaskNouser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TaskSummary> obj = taskService.getTasksOwned(auth.getName(), "en_US");
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "getMyOwnedTask")
    public ResponseEntity<Object> getMyOwnedTask(String userId) {
        List<TaskSummary> obj = taskService.getTasksOwned(userId, "en_US");

        return ResponseEntity.ok(obj);

    }

    @GetMapping(value = "getMyOwnedTaskByStatus")
    public ResponseEntity<Object> getMyOwnedTask(String userId, Status status) {

        List<Status> statusList = new ArrayList<>();
        statusList.add(status);
        List<TaskSummary> obj = taskService.getTasksOwnedByStatus(userId, statusList, "en_US");


        return ResponseEntity.ok(obj);
    }


    @GetMapping("startTask")
    public ResponseEntity<Object> startTask(String userId, Long taskId) {

        taskService.start(taskId, userId);

        return ResponseEntity.ok(taskId);
    }

    @PostMapping("completeTask")
    public ResponseEntity<Object> completeTask(String userId, Long taskId, @Valid @RequestBody Map<String, Object> data) {

        taskService.complete(taskId, userId, data);

        return ResponseEntity.ok(taskId);
    }

    @GetMapping("claimTask")
    public ResponseEntity<Object> calimTask(String userId, Long taskId) {
        taskService.claim(taskId, userId);
        return ResponseEntity.ok(taskId);
    }

    @GetMapping("assignTask")
    public ResponseEntity<Object> assignTask(String userId, Long taskId, String targetUserId) {

        taskService.delegate(taskId, userId, targetUserId);

        return ResponseEntity.ok(taskId);

    }


    @GetMapping(value = "getTaskContent")
    public ResponseEntity<Object> getTaskContent(Long taskId) {

        Map<String, Object> map = taskService.getTaskContent(taskId);

        return ResponseEntity.ok(map);
    }

    @GetMapping(value = "getProcessData")
    public ResponseEntity<Object> getProcessData(Long processInstanceId) {
        ProcessInstance obj = kieSession.getProcessInstance(processInstanceId);
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "getAllProcess")
    public ResponseEntity<Object> getAllProcess() {
        Object obj = kieSession.getProcessInstances();
        return ResponseEntity.ok(obj);
    }

}