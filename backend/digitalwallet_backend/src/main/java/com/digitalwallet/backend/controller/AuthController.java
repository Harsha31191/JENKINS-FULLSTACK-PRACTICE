package com.digitalwallet.backend.controller;

import com.digitalwallet.backend.dto.*;
import com.digitalwallet.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    authService.register(req.getFullName(), req.getEmail(), req.getPassword());
    return ResponseEntity.ok(Map.of("message","registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
    String token = authService.loginAndCreateToken(req.getEmail(), req.getPassword());
    return ResponseEntity.ok(new TokenResponse(token));
  }
}
