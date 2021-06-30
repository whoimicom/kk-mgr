package kim.kin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author choky
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@RestController()
public class KkMgrApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkMgrApplication.class, args);
    }

}
