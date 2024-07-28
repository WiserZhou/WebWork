package edu.hitwh.homework.configuration;

import edu.hitwh.homework.configuration.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    // 注入登录拦截器对象
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器，并配置拦截路径和排除路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")         // 对所有路径进行拦截
                .excludePathPatterns("/login"); // 排除登录路径
    }
}
