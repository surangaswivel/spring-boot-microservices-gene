package com.gene.apigateway.config;

import jakarta.ws.rs.core.Context;
import org.json.JSONObject;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;

public class KeyCloakTokenFilter implements WebFilter {
    @Context
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = "" ;

        try {
            // Create the URL object with the endpoint URL
            URL url = new URL("http://localhost:9090/api/v1/auth/getAccessToken");

            // Create the HttpURLConnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", exchange.getRequest().getHeaders().getFirst("Authorization"));

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response from the input stream
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder res = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }
                System.out.println("Response: " + res.toString());
                JSONObject jsonObject = new JSONObject(res.toString());
                token = jsonObject.getString("access_token");

            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exchange.getRequest().getHeaders().set("Authorization","Bearer "+token);
        return chain.filter(exchange);
    }



}
