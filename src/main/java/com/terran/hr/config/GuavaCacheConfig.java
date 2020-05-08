package com.terran.hr.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GuavaCacheConfig {
    @Bean
    public Cache<String, Object> guavaCache(){
        return CacheBuilder.newBuilder().maximumSize(216000).build();
    }
}
