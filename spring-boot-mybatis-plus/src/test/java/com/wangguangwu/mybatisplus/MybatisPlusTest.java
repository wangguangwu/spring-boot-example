package com.wangguangwu.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import com.wangguangwu.mybatisplus.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wangguangwu
 */
@SpringBootTest
class MybatisPlusTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @Test
    void serviceAndMapper() {
        List<UserDO> serviceList = userService.list(null);
        List<UserDO> mapperList = userMapper.selectList(null);
        Assertions.assertEquals(serviceList, mapperList);
        serviceList.forEach(System.out::println);
        System.out.println("==================");
        mapperList.forEach(System.out::println);
    }

    @Test
    void wrapper() {
        // Wrapper 的基本使用
        List<UserDO> list = userMapper.selectList(Wrappers.<UserDO>query().like("user_name", "%五"));
        Assertions.assertEquals(2, list.size());
        list.forEach(System.out::println);
    }

    @Test
    void lambda() {
        // 上述的 Wrapper 中的条件构造是以硬编码注入的，如果 User 的属性发生变化，条件构造器中也需要一个个修改
        List<UserDO> list = userMapper.selectList(Wrappers.lambdaQuery(UserDO.class).like(UserDO::getUserName, "%五"));
        Assertions.assertEquals(2, list.size());
        list.forEach(System.out::println);
    }

    @Test
    void select() {
        // 使用 select 方法，只取出需要的值
        List<UserDO> list = userMapper.selectList(Wrappers.lambdaQuery(UserDO.class).like(UserDO::getUserName, "%五")
                .select(UserDO::getUserName, UserDO::getUserAge));
        Assertions.assertEquals(2, list.size());
        list.forEach(System.out::println);
    }

    @Test
    void joinQuery() {
        // 聚合查询
        // 只能使用普通的 QueryWrapper 进行查询，LambdaQueryWrapper 不支持
        // 因为使用的 MySQL 是 5.7 以后的版本，所以 select 语句中的非聚合列都必须出现在 `GROUP BY` 中
        UserDO user = userMapper.selectOne(Wrappers.<UserDO>query().select("max(id) as id"));
        Assertions.assertNotNull(user);
        System.out.println(user);
    }

    @Test
    void conditionalConstruct() {
        UserDO testUser = new UserDO();
        // 条件成立才会执行后续的语句
        List<UserDO> listOne = userMapper.selectList(Wrappers.lambdaQuery(UserDO.class)
                .like(StringUtils.hasText(testUser.getUserName()), UserDO::getUserName, testUser.getUserName()));
        Assertions.assertEquals(20, listOne.size());

        testUser.setUserName("王五");
        List<UserDO> listTwo = userMapper.selectList(Wrappers.lambdaQuery(UserDO.class)
                .like(StringUtils.hasText(testUser.getUserName()), UserDO::getUserName, testUser.getUserName()));
        Assertions.assertEquals(1, listTwo.size());
    }

    @Test
    void logicDelete() {
        // 逻辑删除
        int delete = userMapper.delete(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, "王五"));
        Assertions.assertEquals(1, delete);
    }

    //    @Rollback(value = false)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    void version() {
        // 同时拿出数据
        UserDO one = userMapper.selectById(1);
        UserDO two = userMapper.selectById(1);
        Assertions.assertEquals(one.getUserAge(), two.getUserAge());
        Assertions.assertEquals(one.getVersion(), two.getVersion());
        System.out.println("before: " + one.getUserAge() + ", version: " + one.getVersion());

        // 加 100
        one.setUserAge(one.getUserAge() + 100);
        int updateOne = userMapper.updateById(one);
        System.out.println("updateOne: " + updateOne);

        // 减去 80
        two.setUserAge(two.getUserAge() - 80);
        int updateTwo = userMapper.updateById(two);
        System.out.println("updateTwo: " + updateTwo);

        // 查看最终结果
        UserDO user = userMapper.selectById(1);
        System.out.println("after: " + user.getUserAge() + ", version: " + user.getVersion());
    }

    @Test
    void normal() {
        for (int i = 0; i < 10; i++) {
            UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_name", "王五");
            // 年龄减一
            wrapper.setSql("user_age = user_age - 1");
            // 设置当前版本号
            UserDO userDO = UserDO.builder().version(1).build();
            // 乐观锁更新
            userMapper.update(userDO, wrapper);
            System.out.println("回填版本号: " + userDO.getVersion());
        }
        UserDO one = userMapper.selectOne(Wrappers.lambdaQuery(UserDO.class)
                .like(UserDO::getUserName, "王五"));
        System.out.println("last version: " + one.getVersion());
    }
}
