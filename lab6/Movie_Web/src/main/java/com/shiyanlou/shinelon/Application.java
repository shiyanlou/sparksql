package com.shiyanlou.shinelon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SpringBootApplication
@ComponentScan(basePackages = "com.shiyanlou.shinelon")
public class Application extends SpringBootServletInitializer {

    /*
     * Create required HandlerMapping, to avoid several default HandlerMapping instances being created
     */
    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();

    }
//
//    /*
//     * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
//     */
    @Bean
    public HandlerAdapter handlerAdapter() {
        return new RequestMappingHandlerAdapter();

    }
//
//    /*
//     * optimization - avoids creating default exception resolvers; not required as the serverless container handles
//     * all exceptions
//     *
//     * By default, an ExceptionHandlerExceptionResolver is created which creates many dependent object, including
//     * an expensive ObjectMapper instance.
//     */
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new HandlerExceptionResolver() {

            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                return null;
            }
        };


    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}