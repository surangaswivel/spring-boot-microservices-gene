package com.gene.apigateway.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class KeyCloakTokenFilter extends OncePerRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = "";

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
        ServerWebExchange originalExchange = exchange;
        exchange.getRequest().mutate().header("Access_Token", "Bearer " + token);
//        exchange.getRequest().getHeaders().add("Access_Token", "Bearer ");
//        exchange.getRequest().getHeaders().add("Access_Token", "Bearer " + token);
        return chain.filter(exchange);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] requests = {"/api/doc-service/sayHelloPublic"};
        List<String> requestList = Arrays.asList(requests);
        return requestList.contains(request.getServletPath());
    }
}
