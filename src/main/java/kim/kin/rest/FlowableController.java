/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package kim.kin.rest;

import org.flowable.engine.*;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author choky
 */
@RestController
@RequestMapping("/api/flowable")
public class FlowableController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final ManagementService managementService;
    private final IdentityService identityService;
    private final HistoryService historyService;
    private final FormService formService;
    private final DynamicBpmnService dynamicBpmnService;

    public FlowableController(RuntimeService runtimeService, RepositoryService repositoryService, TaskService taskService, ManagementService managementService, IdentityService identityService, HistoryService historyService, FormService formService, DynamicBpmnService dynamicBpmnService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.managementService = managementService;
        this.identityService = identityService;
        this.historyService = historyService;
        this.formService = formService;
        this.dynamicBpmnService = dynamicBpmnService;
    }

    @PostMapping(value = "/taskDetail/{executionId}")
    public ResponseEntity<Object> taskDetail(@PathVariable String executionId) {
        Task task = taskService.createTaskQuery().executionId(executionId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String businessKey = processInstance.getBusinessKey();
        Map<String, Object> execMap = runtimeService.getVariables(executionId);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(currentUsername).list();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> query(Authentication authentication) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(currentUsername).list();
        List<Map<String, Object>> list = new ArrayList<>(tasks.size());
        tasks.forEach(task -> {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", task.getId());
            map.put("name", task.getName());
            String description = task.getDescription();
            String processInstanceId = task.getProcessInstanceId();
            String executionId = task.getExecutionId();
            map.put("executionId", executionId);
            map.put("processInstanceId", processInstanceId);
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionId(processInstanceId).list();
            List<ActivityInstance> activityInstanceList = runtimeService.createActivityInstanceQuery().processInstanceId(processInstanceId).list();
            list.add(map);
        });
        ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(list, HttpStatus.OK);
        return objectResponseEntity;
    }

}
