package org.abc_berkut_test_task;

import org.abc_berkut_test_task.config.BotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class AbcBerkutTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbcBerkutTestTaskApplication.class, args);
    }
}
