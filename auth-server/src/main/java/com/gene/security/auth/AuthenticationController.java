package com.gene.security.auth;

import com.gene.security.demo.UserDetailsResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  @Value("${keycloak.tokenUrl}")
  private String userInfoUrl;

  private final RestTemplate restTemplate;

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @GetMapping("/getAccessToken")
  public  ResponseEntity<AuthenticationResponse>  getAccessToken(HttpServletRequest request) {
    var userDetails = getUserDetails(request.getHeader("Authorization"));
    AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setEmail(userDetails.getEmail());
    return ResponseEntity.ok(service.authenticate(authenticationRequest));
  }

  private UserDetailsResponseDto getUserDetails(String token) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity<>(null, headers);
      var responseBody = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, UserDetailsResponseDto.class);
      return Objects.requireNonNull(responseBody.getBody());
    } catch (HttpClientErrorException e) {
      throw new RuntimeException(e);
    }
  }

}
