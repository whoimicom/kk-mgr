package com.whoimi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @author whoimi
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigurerWiiImpl implements WebMvcConfigurer {

    @Value("${kim.kin.file-path}")
    private String filePath;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file:" + filePath.replace("\\", "/");
        registry.addResourceHandler("/file/**").addResourceLocations(path + File.separator).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(0);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index.html");
    }


}
