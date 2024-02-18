package com.psu.controller;

import com.psu.model.AuthRequest;
import com.psu.model.AuthResponse;
import com.psu.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            // Generate JWT token
            String accessToken = jwtUtil.generateToken(authRequest.getUsername(), 1);
            String refreshToken = jwtUtil.generateToken(authRequest.getUsername(), 10);
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody String refreshToken) {
        try {
            if (jwtUtil.validateToken(refreshToken)) {
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                String newAccessToken = jwtUtil.generateToken(username, 1);
                // Optionally, you could also generate a new refresh token and return it here
                return ResponseEntity.ok(new AuthResponse(newAccessToken, null)); // Pass null or the new refresh token
            } else {
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not refresh token: " + e.getMessage());
        }
    }
}


