package com.wangguangwu.mybatisplus.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WrapperTest {

    @Resource
    private UserMapper userMapper;

    QueryWrapper<UserDO> queryWrapper;

    UpdateWrapper<UserDO> updateWrapper;

    @BeforeEach
    void init() {
        queryWrapper = new QueryWrapper<>();
        updateWrapper = new UpdateWrapper<>();
    }

    //============================ SELECT TESTS ================================

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SelectTests {

        @Test
        @Order(1)
        @DisplayName("使用 selectList 查出数据")
        void testSelectList() {
            List<UserDO> list = userMapper.selectList(null);
            list.forEach(System.out::println);
            Assertions.assertEquals(20, list.size());
        }

        @Test
        @Order(2)
        @DisplayName("使用 selectOne 查出多条数据会抛出 TooManyResultsException")
        void testSelectOne() {
            Assertions.assertThrows(TooManyResultsException.class,
                    () -> userMapper.selectOne(null));
        }

        @Test
        @Order(3)
        @DisplayName("使用 selectOne 查出多条数据如何不报错")
        void testSelectOne2() {
            queryWrapper.last("limit 1");
            UserDO one = userMapper.selectOne(queryWrapper);
            Assertions.assertNotNull(one);
        }

        @Test
        @Order(4)
        @DisplayName("使用 eq 做条件查询")
        void testSelectEqList() {
            // 需要硬编码注入
            queryWrapper.eq("user_name", "张三");
            queryWrapper.ge("user_age", 24);
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(1, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(4)
        @DisplayName("传入实体类做条件查询")
        void testSelectDtoList() {
            UserDO user = UserDO.builder().userName("张三").userAge(25).build();
            queryWrapper = new QueryWrapper<>(user);
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(1, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(5)
        @DisplayName("使用 Map + allEq 做条件查询，可以根据条件过滤参数")
        void testSelectMapAndAllEqList() {
            Map<String, Object> map = new HashMap<>();
            map.put("user_name", "张三");
            map.put("user_age", 25);
            map.put("user_address", null);
            // 过滤 user_age 参数，并且过滤为 null 的值
            queryWrapper.allEq((key, value) -> !"user_age".equals(key), map, false);
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(1, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(6)
        @DisplayName("过滤掉不需要的字段")
        void testSelectFilterField() {
            queryWrapper.select(UserDO.class, u -> !Arrays.asList("user_name", "user_age").contains(u.getColumn()));
            List<UserDO> list = userMapper.selectList(queryWrapper);
            list.forEach(user -> Assertions.assertAll(
                    () -> Assertions.assertNull(user.getUserName()),
                    () -> Assertions.assertNull(user.getUserAge())
            ));
        }

        @Test
        @Order(7)
        @DisplayName("在 eq 中进行条件判断，满足条件才会执行")
        void testSelectConditionEq() {
            UserDO user = new UserDO();
            // 从执行 sql 中看，两种条件判断都没执行，说明两种判断方式是等效的
            if (StringUtils.isNotBlank(user.getUserName())) {
                queryWrapper.eq("user_name", user.getUserName());
            }
            queryWrapper.eq(StringUtils.isNotBlank(user.getUserName()), "user_name", user.getUserName());
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(20, list.size());
        }

        @Test
        @Order(8)
        @DisplayName("在 in 中进行条件判断，满足条件才会执行")
        void testSelectConditionIn() {
            List<String> idList = Collections.emptyList();
            if (!CollectionUtils.isEmpty(idList)) {
                queryWrapper.in("id", idList);
            }
            queryWrapper.in(!CollectionUtils.isEmpty(idList),
                    "id", idList);
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(20, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(9)
        @DisplayName("多条件查询-nested")
        void testSelectMultipleConditionsNested() {
            queryWrapper
                    // 嵌套条件
                    // user_age 大于 35 或者 user_name 为 null 或者 user_name <> 朱二十二
                    .nested(u -> u.gt("user_age", 35)
                            .or().isNull("user_name")
                            .or().ne("user_name", "朱二十二"))
                    // 模糊查询，名字为 张%
                    .likeRight("user_name", "张");
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(1, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(10)
        @DisplayName("多条件查询-and")
        void testSelectMultipleConditionsAnd() {
            queryWrapper
                    // 模糊查询，名字为 张%
                    .likeRight("user_name", "张")
                    // user_age 大于 35 或者 user_name 为 null 或者 user_name <> 朱二十二
                    .and(u -> u.gt("user_age", 35)
                            .or().isNull("user_name")
                            .or().ne("user_name", "朱二十二"));

            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(1, list.size());
            list.forEach(System.out::println);
        }

        @Test
        @Order(11)
        @DisplayName("查询总条数")
        void testSelectCount() {
            Long count = userMapper.selectCount(queryWrapper);
            Assertions.assertEquals(20, count);
        }

        @Test
        @Order(12)
        @DisplayName("返回 maps")
        void testSelectMaps() {
            List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
            Assertions.assertEquals(20, maps.size());
            maps.forEach(System.out::println);
        }

        @Test
        @Order(13)
        @DisplayName("分页")
        void testSelectPage() {
            // 在类 MyBatisPlusConfiguration 中引入了分页插件
            Page<UserDO> page = new Page<>(1, 10);
            Page<UserDO> userPage = userMapper.selectPage(page, queryWrapper);
            Assertions.assertEquals(1, userPage.getCurrent());
            Assertions.assertEquals(2, userPage.getPages());
            Assertions.assertEquals(10, userPage.getSize());
            Assertions.assertEquals(20, userPage.getTotal());
            System.out.println(userPage);
        }

        @Test
        @Order(14)
        @DisplayName("聚合查询")
        void joinQuery() {
            // 聚合查询
            // 只能使用普通的 QueryWrapper 进行查询，LambdaQueryWrapper 不支持
            // 因为使用的 MySQL 是 5.7 以后的版本，所以 select 语句中的非聚合列都必须出现在 `GROUP BY` 中
            UserDO user = userMapper.selectOne(Wrappers.<UserDO>query().select("max(id) as id"));
            Assertions.assertNotNull(user);
            System.out.println(user);
        }

        @Test
        @Order(15)
        @DisplayName("简单查询示例")
        void testSelectExample() {
            // 在查询中选择 user_name 和 user_age 列
            queryWrapper.select("user_name", "user_age")
                    // 在 user_name 列上使用右模糊匹配，查找以“张”开头的用户
                    .likeRight("user_name", "张")
                    // 或者在 user_age 列上查找大于等于 30 的用户
                    .or().ge("user_age", 30)
                    // 按 user_name，user_age 列对结果进行分组
                    .groupBy("user_name", "user_age")
                    // 先按 user_name 列降序排序，再按 user_age 列降序排序
                    .orderByDesc("user_name")
                    .orderByDesc("user_age");
            List<UserDO> list = userMapper.selectList(queryWrapper);
            Assertions.assertEquals(9, list.size());
            list.forEach(System.out::println);
        }
    }

    //============================ DELETE TESTS ================================

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Transactional
    class DeleteTests {

        @Test
        @Order(1)
        @DisplayName("根据 id 删除一条数据")
        void testDeleteById() {
            int deleteCount = userMapper.deleteById(1L);
            Assertions.assertEquals(1, deleteCount);
        }

        @Test
        @Order(2)
        @DisplayName("根据条件删除多条数据")
        void testDeleteByWrapper() {
            // 删除 user_name = "张三" 的记录
            queryWrapper.eq("user_name", "张三");
            int deleteCount = userMapper.delete(queryWrapper);
            Assertions.assertEquals(1, deleteCount);
        }

        @Test
        @Order(3)
        @DisplayName("根据 id 批量删除数据")
        void testDeleteBatchIds() {
            List<Long> ids = Arrays.asList(2L, 3L, 4L);
            int deleteCount = userMapper.deleteBatchIds(ids);
            Assertions.assertEquals(3, deleteCount);
        }
    }

    //============================ UPDATE TESTS ================================

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Transactional
    class UpdateTests {

        @Test
        @Order(1)
        @DisplayName("根据 Wrapper 条件批量更新")
        void testUpdateByWrapper() {
            // 将 user_age = 25 的用户名改为 张三丰
            updateWrapper.eq("user_age", 25)
                    .set("user_name", "张三丰");
            int updateCount = userMapper.update(null, updateWrapper);
            Assertions.assertEquals(3, updateCount);
        }

        @Test
        @Order(2)
        @DisplayName("根据 id 更新一条数据")
        void testUpdateById() {
            UserDO user = new UserDO();
            user.setId(5L);
            user.setUserAge(30);
            int updateCount = userMapper.updateById(user);
            Assertions.assertEquals(1, updateCount);
        }
    }

    //============================ INSERT TESTS ================================

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Transactional
    class insertTests {

        @Test
        @Order(1)
        @DisplayName("插入一条数据")
        void testInsert() {
            UserDO user = UserDO.builder().userName("wang").userAge(24).build();
            int insert = userMapper.insert(user);
            Assertions.assertEquals(1, insert);
        }
    }
}
