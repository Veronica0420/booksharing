package com.ecust.sharebook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.ecust.sharebook.mapper")
@EnableCaching
@SpringBootApplication
public class SharebookApplication{
    public static void main(String[] args) {
        SpringApplication.run(SharebookApplication.class, args);
    }


}

/**
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.ecust.sharebook.mapper")
@EnableCaching
@SpringBootApplication
public class SharebookApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SharebookApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SharebookApplication.class);
    }

}
**/