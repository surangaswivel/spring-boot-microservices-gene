package com.gene.security.demo;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/demo-controller")
@Hidden
public class DemoController {

  @GetMapping
  @RequestMapping("/sayHello")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }

  @GetMapping
  @RequestMapping("/deleteDocument")
  @PreAuthorize("hasAuthority('deleteDocument')")
  public ResponseEntity<String> deleteDocument() {
    return ResponseEntity.ok("deleteDocument endpoint");
  }

  @GetMapping
  @RequestMapping("/addDocument")
  @PreAuthorize("hasAuthority('addDocument')")
  public ResponseEntity<String> addDocument() {
    return ResponseEntity.ok("addDocument endpoint");
  }

  @GetMapping
  @RequestMapping("/updateDocument")
  @PreAuthorize("hasAuthority('updateDocument')")
  public ResponseEntity<String> updateDocument() {
    return ResponseEntity.ok("updateDocument endpoint");
  }

}
