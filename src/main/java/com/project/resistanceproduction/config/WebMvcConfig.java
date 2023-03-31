package com.project.resistanceproduction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig
 * @Description 图片静态资源服务器映射
 * @Author sd
 * @Date 2023/03/30 15:59
 * @Version 1.0
 */
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {
    @Value("${filePath}")
    String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/**是静态映射， file:/root/images/是文件在服务器的路径
        registry.addResourceHandler("/images/**/**.*")
                .addResourceLocations("file:"+filePath);
    }
}