package com.linhhn.zync.user;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class MongoDbTestBase {
    @Container
    static MongoDBContainer database = new MongoDBContainer("mongo:8.0.3");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        database.start();
//        registry.add("spring.data.mongodb.host", database::getContainerIpAddress);
        registry.add("spring.data.mongodb.port", () -> database.getMappedPort(27017));
        registry.add("spring.data.mongodb.database", () -> "test");
    }
}
