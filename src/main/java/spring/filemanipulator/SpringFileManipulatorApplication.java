package spring.filemanipulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

// disable default spring whitelabel page /error basicErrorController
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class SpringFileManipulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFileManipulatorApplication.class, args);
    }
}