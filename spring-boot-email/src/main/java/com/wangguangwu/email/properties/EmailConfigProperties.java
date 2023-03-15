package com.wangguangwu.email.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangguangwu
 */
@Data
@ConfigurationProperties(prefix = "spring.mail")
@Configuration
public class EmailConfigProperties {

    private String host;
    private String username;
    private String password;
    private int port;

    private MailProperties properties;

    public EmailConfigProperties(@Qualifier(value = "mailProperties") MailProperties properties) {
        this.properties = properties;
    }
}
