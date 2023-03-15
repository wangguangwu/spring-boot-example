package com.wangguangwu.email.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author wangguangwu
 */
@SpringBootTest
class EmailServiceTest {

    @Resource
    private EmailService emailService;

    @Disabled
    @MethodSource("dataProvider1")
    @ParameterizedTest
    void sendEmailStandard(String to, String cc, String subject, String content) {
        emailService.sendEmailStandard(to, cc, subject, content);
    }

    @Disabled
    @MethodSource("dataProvider2")
    @ParameterizedTest
    void sendEmailWithHtml(String to, String cc, String subject, String content) {
        emailService.sendEmailWithHtml(to, cc, subject, content);
    }

    @Disabled
    @MethodSource("dataProvider3")
    @ParameterizedTest
    void sendEmailWithAttachment(String to, String cc, String subject, String content, String attachmentPath) {
        emailService.sendEmailWithAttachment(to, cc, subject, content, attachmentPath);
    }

    @Disabled
    @MethodSource("dataProvider4")
    @ParameterizedTest
    void sendEmailWithImages(String to, String cc, String subject, String content, List<String> images) {
        emailService.sendEmailWithImages(to, cc, subject, content, images);
    }

    @Disabled
    @MethodSource("dataProvider5")
    @ParameterizedTest
    void sendEmailWithTemplateByThymeleaf(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> images, String filePath) {
        emailService.sendEmailWithTemplateByThymeleaf(to, cc, subject, templateName, model, images, filePath);
    }

    @Disabled
    @MethodSource("dataProvider6")
    @ParameterizedTest
    void sendEmailWithTemplateByFreemarker(String to, String cc, String subject, String templateName, Map<String, Object> model, Map<String, String> images, String filePath) {
        emailService.sendEmailWithTemplateByFreemarker(to, cc, subject, templateName, model, images, filePath);
    }

    //========================================= 数据源 ================================================

    static Stream<Arguments> dataProvider1() {
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", "Hello World")
        );
    }

    static Stream<Arguments> dataProvider2() {
        StringBuilder htmlBuilder = new StringBuilder();
        try {
            Class<EmailServiceTest> aClass = EmailServiceTest.class;
            InputStream inputStream = aClass.getClassLoader().getResourceAsStream("html/demo.html");
            Assertions.assertNotNull(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                htmlBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = htmlBuilder.toString();
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", html)
        );
    }

    static Stream<Arguments> dataProvider3() {
        String filePath = "";
        try {
            Class<EmailServiceTest> aClass = EmailServiceTest.class;
            filePath = Objects.requireNonNull(aClass.getClassLoader().getResource("excel/hello.xlsx")).getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", "hello", filePath)
        );
    }

    static Stream<Arguments> dataProvider4() {
        String image1 = "image/aa.jpg";
        String image2 = "image/bb.jpg";
        StringBuilder htmlBuilder = new StringBuilder("<p>hello，这是一封测试邮件</p>");
        String[] images = {image1, image2};
        List<String> imagesPath = new ArrayList<>();
        try {
            Class<EmailServiceTest> aClass = EmailServiceTest.class;
            for (String image : images) {
                String imagePath = Objects.requireNonNull(aClass.getClassLoader().getResource(image)).getFile();
                imagesPath.add(imagePath);
                htmlBuilder.append("<p>").append(image).append("</p><img src='cid:").append(imagePath).append("'/>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", htmlBuilder.toString(), imagesPath)
        );
    }

    static Stream<Arguments> dataProvider5() {
        String image1 = "image/aa.jpg";
        String image2 = "image/bb.jpg";
        String filePath = "excel/hello.xlsx";
        String[] images = {image1, image2};
        Map<String, String> imagesPath = new HashMap<>(16);
        try {
            Class<EmailServiceTest> aClass = EmailServiceTest.class;
            filePath = Objects.requireNonNull(aClass.getClassLoader().getResource(filePath)).getFile();
            for (String image : images) {
                String imagePath = Objects.requireNonNull(aClass.getClassLoader().getResource(image)).getFile();
                imagesPath.put(image, imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wangguangwu");
        map.put("blog", "https://www.wangguangwu.com");
        map.put("images", Arrays.asList(images));
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", "thymeleaf.html", map, imagesPath, filePath)
        );
    }

    static Stream<Arguments> dataProvider6() {
        String image1 = "image/aa.jpg";
        String image2 = "image/bb.jpg";
        String filePath = "excel/hello.xlsx";
        String[] images = {image1, image2};
        Map<String, String> imagesPath = new HashMap<>(16);
        try {
            Class<EmailServiceTest> aClass = EmailServiceTest.class;
            filePath = Objects.requireNonNull(aClass.getClassLoader().getResource(filePath)).getFile();
            for (String image : images) {
                String imagePath = Objects.requireNonNull(aClass.getClassLoader().getResource(image)).getFile();
                imagesPath.put(image, imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wangguangwu");
        map.put("blog", "https://www.wangguangwu.com");
        map.put("images", Arrays.asList(images));
        return Stream.of(
                arguments("wangguangwu@pukanghealth.com", "1109294584@qq.com", "subject", "freemarker.html", map, imagesPath, filePath)
        );
    }
}
