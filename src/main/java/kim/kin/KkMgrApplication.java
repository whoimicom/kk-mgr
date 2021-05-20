package kim.kin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author choky
 */
@SpringBootApplication
@RestController()
public class KkMgrApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkMgrApplication.class, args);
    }

//    @GetMapping("/")
//    public String index() {
//        return "kk service started successfully";
//    }

}
