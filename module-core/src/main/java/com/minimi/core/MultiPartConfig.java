package com.minimi.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Slf4j
@Configuration
public class MultiPartConfig {
    @Value("${spring.servlet.multipart.location}")
    private String fileTempDir;

    @Bean
    MultipartConfigElement multipartConfig() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("win")) {
            fileTempDir = "C:" + fileTempDir;
        }
        log.info("fileTempDir : {}",fileTempDir);
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File tempDir = new File(fileTempDir);
        if(!tempDir.exists()){
            boolean mkdirsSuccess = tempDir.mkdirs();
            log.info("mkdirsSuccess : {}",mkdirsSuccess);
        }
        factory.setLocation(fileTempDir);
        return factory.createMultipartConfig();
    }
}
