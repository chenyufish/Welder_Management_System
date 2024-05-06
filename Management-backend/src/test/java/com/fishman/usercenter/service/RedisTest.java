package com.fishman.usercenter.service;

import com.fishman.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @author  fishman
 * @Description:     Redis测试
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("fishmanString", "fish");
        valueOperations.set("fishmanInt", 1);
        valueOperations.set("fishmanDouble", 2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("fishman");
        valueOperations.set("fishmanUser", user);

        // 查
        Object fishman = valueOperations.get("fishmanString");
        Assertions.assertTrue("fish".equals((String) fishman));
        fishman = valueOperations.get("fishmanInt");
        Assertions.assertTrue(1 == (Integer) fishman);
        fishman = valueOperations.get("fishmanDouble");
        Assertions.assertTrue(2.0 == (Double) fishman);
        System.out.println(valueOperations.get("fishmanUser"));
        valueOperations.set("fishmanString", "fish");

    }

}
