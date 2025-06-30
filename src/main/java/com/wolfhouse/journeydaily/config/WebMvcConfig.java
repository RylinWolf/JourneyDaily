package com.wolfhouse.journeydaily.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfhouse.journeydaily.common.constant.CommonConstant;
import com.wolfhouse.journeydaily.interceptors.JwtInterceptor;
import com.wolfhouse.journeydaily.interceptors.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linexsong
 */
@Configuration
@ComponentScan("com.wolfhouse.journeydaily.controller")
@Slf4j
@RequiredArgsConstructor
public class WebMvcConfig extends WebMvcConfigurationSupport {
    private final JwtInterceptor jwtInterceptor;
    private final TokenInterceptor tokenInterceptor;
    private final ObjectMapper jacksonObjectMapper;
    @Resource(name = "jacksonSnakeCaseObjectMapper")
    private final ObjectMapper snakeCaseObjectMapper;
    @Resource(name = "nonNullObjectMapper")
    private final ObjectMapper nonNullObjectMapper;

    @Bean
    public Docket customDocket() {
        log.info("注册 Knife4j 文档功能...");
        ApiInfo apiInfo = new ApiInfoBuilder().title("日日记接口文档")
                                              .description("日日记接口文档")
                                              .version("1.0")
                                              .build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                                                      .select()
                                                      .apis(RequestHandlerSelectors.basePackage(
                                                              "com.wolfhouse.journeydaily.controller"))
                                                      .paths(PathSelectors.any())
                                                      .build();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/admin/**")
                .addPathPatterns("/journey/**")
                .addPathPatterns("/jpt/**")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/admin/add")
                .excludePathPatterns("/admin/recovery/**")
                // 查看日记无须登录
                .excludePathPatterns("/journey/*");

        registry.addInterceptor(tokenInterceptor).addPathPatterns("/journeys").addPathPatterns("/journeys/*")
                // 查看日记时注入 token 信息以显示当前登录用户的私密日记
                .addPathPatterns("/journey/*");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 设置标准 Json 消息转换器
        MappingJackson2HttpMessageConverter simpleConverter = new MappingJackson2HttpMessageConverter();
        simpleConverter.setObjectMapper(jacksonObjectMapper);

        // 设置忽略空字段的 Json 消息转换器
        MappingJackson2HttpMessageConverter notNonConverter = new MappingJackson2HttpMessageConverter();
        notNonConverter.setObjectMapper(nonNullObjectMapper);
        notNonConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(
                "application/" + CommonConstant.MEDIA_TYPE_PATCH));

        // 添加以上消息转换器
        converters.add(0, simpleConverter);
        converters.add(1, notNonConverter);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:[*]")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
