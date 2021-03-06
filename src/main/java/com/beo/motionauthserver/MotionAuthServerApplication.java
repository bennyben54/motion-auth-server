package com.beo.motionauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@EnableWebSecurity
@SpringBootApplication
public class MotionAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotionAuthServerApplication.class, args);
    }

}
