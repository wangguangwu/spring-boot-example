package com.wangguangwu.email.config;

import com.wangguangwu.email.properties.EmailConfigProperties;
import com.wangguangwu.email.properties.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author wangguangwu
 */
@EnableConfigurationProperties({EmailConfigProperties.class, MailProperties.class})
@Configuration
public class EmailConfig {

    @Resource
    private EmailConfigProperties emailConfigProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfigProperties.getHost());
        mailSender.setPort(emailConfigProperties.getPort());
        mailSender.setUsername(emailConfigProperties.getUsername());
        mailSender.setPassword(emailConfigProperties.getPassword());
        MailProperties mailProperties = emailConfigProperties.getProperties();
        Properties properties = mailProperties.javaMailProperties();
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
