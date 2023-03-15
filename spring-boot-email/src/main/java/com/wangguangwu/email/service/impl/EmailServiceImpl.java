package com.wangguangwu.email.service.impl;

import com.wangguangwu.email.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * @author wangguangwu
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void sendEmailStandard(String to, String cc, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // 配置编码格式是 utf-8
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            helper.setText(content);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithHtml(String to, String cc, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            // 发送 html 文件
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String cc, String subject, String content, String attachmentPath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithImages(String to, String cc, String subject, String content, List<String> images) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            helper.setText(content, true);
            for (String image : images) {
                FileSystemResource resource = new FileSystemResource(new File(image));
                helper.addInline(image, resource);
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithTemplateByThymeleaf(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> imageMap, String filePath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            // 配置模版参数
            Context context = getContext(model);
            // 映射模版
            String process = templateEngine.process(templateName, context);
            // 设置为 html 格式
            helper.setText(process, true);
            configImage(imageMap, helper);
            // 携带附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(message);
            // 发送邮件
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithTemplateByFreemarker(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> imageMap, String filePath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMessageHelper(to, cc, subject, message);
            // 构建 Freemarker
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            ClassLoader loader = this.getClass().getClassLoader();
            configuration.setClassLoaderForTemplateLoading(loader, "templates");
            Template template = configuration.getTemplate("freemarker.html");
            StringWriter htmlWriter = new StringWriter();
            template.process(model, htmlWriter);
            helper.setText(htmlWriter.toString(), true);
            configImage(imageMap, helper);
            // 携带附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(message);
            // 发送邮件
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    //============================= 私有方法 ===================================

    private MimeMessageHelper getMessageHelper(String to, String cc, String subject, MimeMessage message) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setSentDate(new Date());
        helper.setFrom(username);
        helper.setTo(to);
        if (StringUtils.isNotBlank(cc)) {
            helper.setCc(cc);
        }
        helper.setSubject(subject);
        return helper;
    }

    private static Context getContext(Map<String, Object> model) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }

    private static void configImage(Map<String, String> imageMap, MimeMessageHelper helper) throws MessagingException {
        for (Map.Entry<String, String> entry : imageMap.entrySet()) {
            FileSystemResource aaResource = new FileSystemResource(new File(entry.getValue()));
            helper.addInline(entry.getKey(), aaResource);
        }
    }
}
