package kim.kin.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableJdbcAuditing
class AuditorAwareKimConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorAwareKimImpl();
    }
}