package wang;

import com.wangguangwu.junit.SpringBootJunitApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 和启动类不处于同一个包下
 * <p>
 * `@SpringBoot` 注解表明是一个测试类
 *
 * @author wangguangwu
 */
@SpringBootTest(classes = SpringBootJunitApplication.class)
class OuterTest {

    @Test
    void hello() {
        System.out.println("hello");
    }
}
