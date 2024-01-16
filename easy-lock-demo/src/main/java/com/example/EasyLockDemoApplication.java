package com.example;

import com.easy.lock.annotation.EnableEasyLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEasyLock
@SpringBootApplication
public class EasyLockDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyLockDemoApplication.class, args);
    }

}
