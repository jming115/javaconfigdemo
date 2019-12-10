package com.mfun.javaconfigdemo.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author: JMing
 * @date: 2019/11/15 18:07
 * @description:
 */

@Configuration
@EnableScheduling //启用 @Scheduled 注解，实现定时器功能
@PropertySource({
        "classpath:application.properties",
        "classpath:application-${spring.profiles.active:dev}.properties",
        "classpath:log4j.properties",
})
@ComponentScan(basePackages = {"com.mfun.javaconfigdemo"},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class, Controller.class})
    })
public class RootConfig {
    /**
     * 加载属性文件，同时使@Value注解可用，解析属性占位符
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
