package com.wangguangwu.email.service;

import java.util.List;
import java.util.Map;

/**
 * @author wangguangwu
 */
public interface EmailService {

    /**
     * send email standard
     *
     * @param to      send people
     * @param cc      copy to people
     * @param subject email subject
     * @param content email content
     */
    void sendEmailStandard(String to, String cc, String subject, String content);

    /**
     * send email html
     *
     * @param to      send people
     * @param cc      copy to people
     * @param subject email subject
     * @param content email content
     */
    void sendEmailWithHtml(String to, String cc, String subject, String content);

    /**
     * send email attachment
     *
     * @param to             send people
     * @param cc             copy to people
     * @param subject        email subject
     * @param content        email content
     * @param attachmentPath attachmentPath
     */
    void sendEmailWithAttachment(String to, String cc, String subject, String content, String attachmentPath);

    /**
     * send email images
     *
     * @param to      send people
     * @param cc      copy to people
     * @param subject email subject
     * @param content email content
     * @param images  images
     */
    void sendEmailWithImages(String to, String cc, String subject, String content, List<String> images);

    /**
     * send email by thymeleaf
     *
     * @param to           send people
     * @param cc           copy to people
     * @param subject      email subject
     * @param templateName thymeleaf file name, located in resources/templates
     * @param model        data will inject to the template
     * @param images       image name and location
     * @param filePath     file path
     */
    void sendEmailWithTemplateByThymeleaf(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> images, String filePath);

    /**
     * send email by freemarker
     *
     * @param to           send people
     * @param cc           copy to people
     * @param subject      email subject
     * @param templateName freemarker file name, no demand for location
     * @param model        data will inject to the template
     * @param images       image name and location
     * @param filePath     file path
     */
    void sendEmailWithTemplateByFreemarker(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> images, String filePath);

}
