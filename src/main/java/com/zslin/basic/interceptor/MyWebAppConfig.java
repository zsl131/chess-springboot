package com.zslin.basic.interceptor;

import com.zslin.bus.wx.tools.WxInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zsl-pc on 2016/9/7.
 */
@Configuration
public class MyWebAppConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SystemInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/admin/**");
        registry.addInterceptor(new WxInterceptor()).addPathPatterns("/wxRemote/**", "/wx/**");
        super.addInterceptors(registry);
    }
}
