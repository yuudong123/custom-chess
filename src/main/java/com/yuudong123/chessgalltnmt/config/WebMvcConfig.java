package com.yuudong123.chessgalltnmt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yuudong123.chessgalltnmt.support.interceptor.AdminAccessInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private AdminAccessInterceptor adminAccessInterceptor;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry.addInterceptor(adminAccessInterceptor)
        .order(1)
        .addPathPatterns("/admin/**")
        .excludePathPatterns("/error/**");
  }
}
