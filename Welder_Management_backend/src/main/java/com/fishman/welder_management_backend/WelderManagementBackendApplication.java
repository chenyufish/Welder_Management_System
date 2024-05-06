package com.fishman.welder_management_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@EnableAspectJAutoProxy
@MapperScan("com.fishman.welder_management_backend.mapper")
@EnableScheduling
public class WelderManagementBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WelderManagementBackendApplication.class, args);
    }

}
