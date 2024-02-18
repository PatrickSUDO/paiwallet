package com.psu.controller;

import com.psu.model.User;
import com.psu.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerUser_ReturnsRegisteredUser() {
        User user = new User();
        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void getUserByUsername_ExistingUser_ReturnsUser() {
        User user = new User();
        when(userService.findByUsername("existingUser")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserByUsername("existingUser");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void getUserByUsername_NonExistingUser_ReturnsNotFound() {
        when(userService.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserByUsername("nonExistingUser");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

}
