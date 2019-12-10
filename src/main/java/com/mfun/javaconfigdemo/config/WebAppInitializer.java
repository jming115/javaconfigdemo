package com.mfun.javaconfigdemo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author: JMing
 * @date: 2019/11/25 16:26
 * @description: 应用程序入口
 */

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 指定根应用上下文配置类
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfig.class};
    }

    /**
     * 指定DispatcherServlet配置类
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebConfig.class};
    }

    /**
     * 配置DispatcherServlet映射
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
