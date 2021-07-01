package kim.kin.config;


import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.*;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * @author choky
 */
@Configuration
public class JbpmConfig {

    @Bean
    RuntimeEnvironment runtimeEnvironment(EntityManagerFactory entityManagerFactory) {
        RuntimeEnvironmentBuilder runtimeEnvironmentBuilder = RuntimeEnvironmentBuilder.Factory.get()
                .newDefaultBuilder().entityManagerFactory(entityManagerFactory);

        runtimeEnvironmentBuilder.addAsset(ResourceFactory.newClassPathResource("jbpm/test.bpmn"), ResourceType.BPMN2);

        return runtimeEnvironmentBuilder.get();
    }

    @Bean
    RuntimeManager runtimeManager(RuntimeManagerFactory runtimeManagerFactory, RuntimeEnvironment runtimeEnvironment) {
        return runtimeManagerFactory.newSingletonRuntimeManager(runtimeEnvironment);
    }

    @Bean
    RuntimeEngine runtimeEngine(RuntimeManager runtimeManager) {
        return runtimeManager.getRuntimeEngine(EmptyContext.get());
    }

    @Bean
    KieSession kieSession(RuntimeEngine runtimeEngine) {
        return runtimeEngine.getKieSession();
    }

    @Bean
    TaskService taskService(RuntimeEngine runtimeEngine) {
        return runtimeEngine.getTaskService();
    }

    @Bean
    AuditService auditService(RuntimeEngine runtimeEngine) {
        return runtimeEngine.getAuditService();
    }


//   @Bean
//	@DependsOn("jndiDataSource")
//	UserGroupCallback userGroupCallback() {
//		return new DBUserGroupCallbackImpl(true);
//	}
//

}