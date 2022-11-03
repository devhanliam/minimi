package com.minimi.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {
    @Value("${spring.redis.port}")
    String value;

    @Test
    public void getValue() {

        assertEquals("/Users/han/attach/",value);

    }
}