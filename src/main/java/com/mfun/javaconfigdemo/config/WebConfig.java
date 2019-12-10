package com.mfun.javaconfigdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author: JMing
 * @date: 2019/11/15 11:44
 * @description: SpringMVC配置
 */

@Configuration
@EnableWebMvc
// 仅扫描controller注解的类，不使用默认的过滤规则
@ComponentScan(basePackages = {"com.mfun.javaconfigdemo.controller"},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
    })
@PropertySource({"classpath:application-${spring.profiles.active:dev}.properties"})
public class WebConfig extends WebMvcConfigurerAdapter {
//    private LoginInterceptor loginInterceptor;

    @Value("${cors.allowedOrigins:}")
    private String allowedOrigins;

//    @Autowired
//    public WebConfig(LoginInterceptor loginInterceptor) {
//        this.loginInterceptor = loginInterceptor;
//    }

    /**
     * 配置视图解析器
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        return resolver;
    }

    /**
     * CORS跨域请求支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("OPTIONS","HEAD","GET","POST","PUT","DELETE")
                .allowedOrigins(allowedOrigins.split(","))
                .allowCredentials(true)
                // 缓存预检测结果,这样在一定时间内，
                // 就是第一次发送请求的时候预检测，
                // 后面的就直接发送请求了
                .maxAge(3600);
        super.addCorsMappings(registry);
    }

    /**
     * 配置文件上传，bean的id必须为multipartResolver，否则会造成request转换错误
     */
    @Bean("multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(104857600);
        multipartResolver.setDefaultEncoding("UTF-8");

        /**
         * 延迟文件解析，默认值false。
         * 当 resolveLazily 为 false 时，会立即调用 parseRequest() 方法对请求数据进行解析，
         * 然后将解析结果封装到 DefaultMultipartHttpServletRequest 中；
         * 而当 resolveLazily 为 true 时，会在
         * org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest
         * 的 initializeMultipart() 方法调用 parseRequest() 方法对请求数据进行解析，
         * 而 initializeMultipart() 方法又是被 getMultipartFiles() 方法调用，
         * 即当需要获取文件信息时才会去解析请求数据，这种方式用了懒加载的思想
         */
//        multipartResolver.setResolveLazily(true);

        return multipartResolver;
    }

    /**
     * 使静态资源的请求直接使用默认servlet处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 配置拦截器
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
//    }
}
