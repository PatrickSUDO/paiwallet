package com.psu.service;

import com.psu.model.User;
import com.psu.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerUser_SuccessfullyRegistersUser() {
        User user = new User();
        user.setPassword("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = service.registerUser(user);

        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(any(User.class));
    }
}
