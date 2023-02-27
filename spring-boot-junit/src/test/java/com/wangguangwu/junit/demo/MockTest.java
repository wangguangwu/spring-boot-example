package com.wangguangwu.junit.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author wangguangwu
 */
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MockTest {

    @Resource
    private MockMvc mockMvc;

    @Test
    void mockMvc() throws Exception {
        // 使用 mockMvc 模拟了一个 GET 请求
        // 验证了响应的状态码为 200，响应的返回结果是 Hello World
        mockMvc.perform(get("/hello/wang"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }
}
