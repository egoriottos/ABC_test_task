package org.abc_berkut_test_task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "telegram")
public class BotProperties {
    private String botName;

    private String botToken;
}
