package com.aasrivas.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedit() {
        redisTemplate.opsForValue().set("aadesh", "sigma");
        Object aadesh = redisTemplate.opsForValue().get("aadesh");
        System.out.println(aadesh);
    }
}
