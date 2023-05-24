package com.gene.documentservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionFilter extends OncePerRequestFilter {


    private final String secretKey;

    public PermissionFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Access_Token").substring(7);
//        String jwtToken1 = response.getHeader("access_token").substring(7);
        if (jwtToken != null) {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            String userName = decodedJWT.getClaim("username").toString();
            var roles = decodedJWT.getClaim("permissions").asList(String.class);
            List<SimpleGrantedAuthority> authorityList = roles.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userName, null, authorityList));
        } else {
            throw new BadCredentialsException("Invalid JWT token");
        }
        filterChain.doFilter(request, response);
    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization").substring(7);
//        if (jwtToken != null) {
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
//            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
//            String userName = decodedJWT.getClaim("username").toString();
//            var roles = decodedJWT.getClaim("permissions").asList(String.class);
//            List<SimpleGrantedAuthority> authorityList = roles.stream().map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//            SecurityContextHolder.getContext()
//                    .setAuthentication(new UsernamePasswordAuthenticationToken(userName, null, authorityList));
//        } else {
//            throw new BadCredentialsException("Invalid JWT token");
//        }
//        return chain.filter(exchange);
//    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/api/doc-service/sayHelloPublic");
    }
}
