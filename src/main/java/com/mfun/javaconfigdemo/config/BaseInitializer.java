package com.mfun.javaconfigdemo.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

/**
 * @author: JMing
 * @date: 2019/11/15 19:21
 * @description: app启动时一些基本的初始化工作，可以在此添加servlet和filter
 */

public class BaseInitializer implements WebApplicationInitializer {
    private static final String DEFAULT_PROFILE = "dev";

    @Override
    public void onStartup(ServletContext servletContext) {
        // 字符集过滤器
        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("charEncodingFilter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        // 支持Restful请求
        FilterRegistration.Dynamic hiddenHttpMethodFilter =
                servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
        hiddenHttpMethodFilter.addMappingForUrlPatterns(null, false, "/*");

        // 设置默认profile
        servletContext.setInitParameter("spring.profiles.default", DEFAULT_PROFILE);
    }
}
