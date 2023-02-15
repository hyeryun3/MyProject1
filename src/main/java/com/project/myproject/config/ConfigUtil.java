package com.project.myproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:admin.properties")
public class ConfigUtil {

    private final Environment env;

    public String getAdminId(){
        return env.getProperty("adminId");
    }

    public String getAdminPw(){
        return env.getProperty("adminPw");
    }

}
