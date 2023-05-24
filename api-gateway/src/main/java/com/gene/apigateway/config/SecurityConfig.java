package com.gene.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${gene.authorization.tokenUrl}")
    String accessTokenUrl;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .addFilterAfter(new KeyCloakTokenFilter(accessTokenUrl), SecurityWebFiltersOrder.LAST)
                .authorizeExchange(exchange -> exchange.pathMatchers("/api/doc-service/sayHelloPublic").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        serverHttpSecurity.csrf().disable();
        return serverHttpSecurity.build();
    }

}
