package com.bilibili;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
@SpringBootApplication
public class BilibiliAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliAdminApplication.class, args);
    }

}
