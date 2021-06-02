package kim.kin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author choky
 */
@SpringBootApplication
@EnableJdbcRepositories
@RestController()
public class KkMgrApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkMgrApplication.class, args);
    }

}
