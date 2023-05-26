package com.gene.apigateway.config;

import com.gene.apigateway.response.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KeyCloakTokenFilter  implements WebFilter {

    private final String accessTokenUrl;
    @Autowired
    private RestTemplate restTemplate;

    public KeyCloakTokenFilter(String accessTokenUrl) {
        this.restTemplate = new RestTemplate();
        this.accessTokenUrl = accessTokenUrl;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (shouldFilter(exchange.getRequest())) {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            var tokenResponse = getAuthorizeToken(token);
            ServerWebExchange originalExchange = exchange;
            exchange.getRequest().mutate().header("Access_Token", "Bearer " + tokenResponse.getAccess_token());

        }
        return chain.filter(exchange);
    }




    protected boolean shouldFilter(ServerHttpRequest request) {
        String[] requests = {"/api/doc-service/sayHelloPublic"};
        List<String> requestList = Arrays.asList(requests);
        return ! requestList.contains(request.getPath().toString());
    }

    private TokenResponse getAuthorizeToken(String keyCloakToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", keyCloakToken);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            var responseBody = restTemplate.exchange(accessTokenUrl, HttpMethod.GET, entity, TokenResponse.class);
            return Objects.requireNonNull(responseBody.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
