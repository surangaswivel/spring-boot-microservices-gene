package com.gene.apigateway.config;

import jakarta.ws.rs.core.Context;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;

public class KeyCloakTokenFilter implements WebFilter {
    @Context
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication.getPrincipal();
        String userIdByToken = "";
        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
            IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
            userIdByToken = token.getSubject();
        }
        String accessToken = exchange.getResponse().getHeaders().getFirst("access_token");
//        RSAKey key = new RSAKey()
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256(new RSAKey())).build();
//        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return chain.filter(exchange);
    }



}
