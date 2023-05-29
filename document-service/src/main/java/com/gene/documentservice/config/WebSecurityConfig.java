package com.gene.documentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private final String secretKey;

    public WebSecurityConfig(@Value("${application.security.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterAfter(new PermissionFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/api/doc-service/sayHelloPublic").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().and()
                .httpBasic();
        return http.build();
    }
}
