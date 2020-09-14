package com.store.conf;

import com.store.interceptor.LoginInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;

@RestController
@ComponentScan("com.store")
@EntityScan({"com.store.data.entity"})
@EnableJpaRepositories({"com.store.repository", "com.store.common.repository"})
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    @Inject
    private LoginInterceptor loginInterceptor;

    @RequestMapping("/home")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    // 不使用session
//    @Bean
//    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
//        return (ConfigurableEmbeddedServletContainer container) -> {
//            container.setSessionTimeout(60, TimeUnit.MINUTES);
//        };
//    }
}