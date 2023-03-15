package com.wangguangwu.email.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wangguangwu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mail.properties.mail.smtp")
public class MailProperties {

    private boolean auth;
    private boolean starttlsEnable;
    private boolean starttlsRequired;
    private boolean sslEnable;
    private String socketFactoryClass;
    private boolean socketFactoryFallback;
    private int socketFactoryPort;

    @Bean
    public Properties javaMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        properties.put("mail.smtp.ssl.enable", sslEnable);
        properties.put("mail.smtp.socketFactory.class", socketFactoryClass);
        properties.put("mail.smtp.socketFactory.fallback", socketFactoryFallback);
        properties.put("mail.smtp.socketFactory.port", socketFactoryPort);
        return properties;
    }
}

