//package com.gene.documentservice.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
////@Configuration
////@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        serverHttpSecurity
//                .addFilterBefore(new PermissionFilter(), SecurityWebFiltersOrder.LAST)
//                .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//
//        serverHttpSecurity.csrf().disable();
//        return serverHttpSecurity.build();
//    }
//
//    @Bean
//    public ReactiveJwtDecoder jwtDecoder() {
//        return NimbusReactiveJwtDecoder.withJwkSetUri("http://localhost:8089/auth/realms/gene-project/protocol/openid-connect/certs").build();
//    }
//}
