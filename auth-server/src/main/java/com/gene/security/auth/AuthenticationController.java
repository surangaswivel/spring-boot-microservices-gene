package com.gene.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

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
  public  ResponseEntity<AuthenticationResponse>  getAccessToken(
          HttpServletRequest request
  ) {
    System.out.println(" >>>>>>>>>>>>> " );
    String email = "";

    try {
      // Create the URL object with the endpoint URL
      URL url = new URL("http://localhost:8089/auth/realms/gene-project/protocol/openid-connect/userinfo");

      // Create the HttpURLConnection object
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set the request method to GET
      connection.setRequestMethod("GET");

      connection.setRequestProperty("Authorization", request.getHeader("Authorization"));

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
         email = jsonObject.getString("preferred_username");

      }

      // Disconnect the connection
      connection.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }

    AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setEmail(email);
    return ResponseEntity.ok(service.authenticate(authenticationRequest));
  }

}
