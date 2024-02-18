package com.psu.service;

import com.psu.model.User;
import com.psu.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadUserByUsername_WhenUserExists() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("testUser");

        assertThat(userDetails.getUsername()).isEqualTo("testUser");
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("nonExistentUser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with username: nonExistentUser");
    }
}
