package com.psu.controller;

import com.psu.model.AuthRequest;
import com.psu.model.AuthResponse;
import com.psu.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void login_SuccessfulAuthentication_ReturnsAuthResponse() {
        AuthRequest request = new AuthRequest("user", "password");
        Authentication authentication = mock(Authentication.class); // Assuming this to be your authenticated object
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken("user", 1)).thenReturn("accessToken");
        when(jwtUtil.generateToken("user", 10)).thenReturn("refreshToken");

        ResponseEntity<?> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void login_UnsuccessfulAuthentication_ReturnsBadRequest() {
        AuthRequest request = new AuthRequest("user", "wrongPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void refreshAccessToken_ValidRefreshToken_ReturnsNewAccessToken() {
        String refreshToken = "validRefreshToken";
        String username = "user";
        String newAccessToken = "newAccessToken";
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(refreshToken)).thenReturn(username);
        when(jwtUtil.generateToken(username, 1)).thenReturn(newAccessToken);

        ResponseEntity<?> response = authController.refreshAccessToken(refreshToken);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(AuthResponse.class);
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertThat(authResponse.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(authResponse.getRefreshToken()).isNull();
    }

    @Test
    void refreshAccessToken_InvalidRefreshToken_ReturnsBadRequest() {
        String invalidRefreshToken = "invalidRefreshToken";
        when(jwtUtil.validateToken(invalidRefreshToken)).thenReturn(false);

        ResponseEntity<?> response = authController.refreshAccessToken(invalidRefreshToken);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Invalid refresh token");
    }


}
