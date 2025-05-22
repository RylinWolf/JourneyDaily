package com.wolfhouse.journeydaily.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author linexsong
 */
@Configuration
public class MyBatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 初始化分页插件
        PaginationInnerInterceptor pag = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置分页上限
        pag.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(pag);
        return interceptor;
    }
}
