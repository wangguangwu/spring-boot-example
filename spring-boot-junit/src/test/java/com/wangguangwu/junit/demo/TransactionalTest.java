package com.wangguangwu.junit.demo;

import com.wangguangwu.junit.entity.UserDO;
import com.wangguangwu.junit.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangguangwu
 */
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionalTest {

    @Resource
    public UserService userService;

    @Test
    void save() {
        UserDO user = new UserDO();
        user.setUserName("test");
        user.setUserAge((byte) 24);
        userService.save(user);
        List<UserDO> users = userService.list();
        System.out.println("size: " + users.size());
    }

    @Commit
    @Test
    void update() {
        UserDO user = new UserDO();
        user.setId(1L);
        user.setUserName("test");
        user.setUserAge((byte) 24);
        userService.updateById(user);
        List<UserDO> users = userService.list();
        System.out.println("size: " + users.size());
    }

    @Rollback(value = false)
    @Test
    void update2() {
        UserDO user = new UserDO();
        user.setId(2L);
        user.setUserName("hello");
        user.setUserAge((byte) 24);
        userService.updateById(user);
        List<UserDO> users = userService.list();
        System.out.println("size: " + users.size());
    }

    @Nested
    class InnerTest {

        @Test
        void inner() {
            UserDO user = new UserDO();
            user.setUserName("test");
            user.setUserAge((byte) 24);
            userService.save(user);
            List<UserDO> users = userService.list();
            System.out.println("size: " + users.size());
        }
    }
}
