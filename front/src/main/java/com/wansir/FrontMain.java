package com.wansir;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.wansir.mapper")
@EnableSwagger2     //开启swagger文档功能
@EnableScheduling //开启定时任务功能支持
public class FrontMain {
    public static void main(String[] args) {
        SpringApplication.run(FrontMain.class, args);
    }
}