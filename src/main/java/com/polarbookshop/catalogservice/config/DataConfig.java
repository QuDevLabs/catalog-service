package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing // 在Spring Data JDBC中启用审计功能，允许自动记录实体的创建和修改信息
public class DataConfig {

    @Bean
    AuditorAware<String> auditorAware() { // 返回当前认证用户的用户名，以便在审计字段中使用
        return () ->

                Optional.ofNullable(SecurityContextHolder.getContext()) // 从SecurityContextHolder中为已认证的用户提取SecurityContext对象
                        .map(SecurityContext::getAuthentication) // 从SecurityContext对象中提取Authentication对象
                        .filter(Authentication::isAuthenticated) // 处理用户未经认证但尝试操作数据的场景。因为我们保护了所有端点，所以这种情况永远不会发生，但是为了完整性，我们包含这种情况
                        .map(Authentication::getName); // 从Authentication对象中提取用户名
    }

}
