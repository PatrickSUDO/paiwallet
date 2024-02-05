package com.psu.service;

import com.psu.model.User;
import com.psu.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setPassword("testPassword");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword"); // Ensure the stub matches the password

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertThat(savedUser).isNotNull();
        verify(userRepository).save(user);
        verify(passwordEncoder).encode("testPassword");
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";
        User user = new User(); // Set user details
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        assertThat(userService.findByUsername(username)).isPresent();
        verify(userRepository).findByUsername(username);
    }
}
