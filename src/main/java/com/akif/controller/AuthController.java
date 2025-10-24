package com.akif.controller;

import com.akif.dto.request.LoginRequestDto;
import com.akif.dto.request.RefreshTokenRequestDto;
import com.akif.dto.request.RegisterRequestDto;
import com.akif.dto.response.AuthResponseDto;
import com.akif.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        log.info("POST /api/auth/register - Registering user: {}", registerRequest.getUsername());
        
        AuthResponseDto response = authService.register(registerRequest);
        
        log.info("User registered successfully: {}", registerRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("POST /api/auth/login - User login attempt: {}", loginRequest.getUsername());
        
        AuthResponseDto response = authService.login(loginRequest);
        
        log.info("User logged in successfully: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Get new access token using refresh token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        log.info("POST /api/auth/refresh - Refreshing token");
        
        AuthResponseDto response = authService.refreshToken(refreshTokenRequest);
        
        log.info("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }
}
