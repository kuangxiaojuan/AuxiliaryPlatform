package com.terran.hr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component@Data
@ConfigurationProperties(prefix = "hr")
public class HrDataSource {
    private String driverCLass;
    private String url;
    private String username;
    private String password;
}
