package com.psu.repository;

import com.psu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("testUser@example.com");
        userRepository.save(user);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        Optional<User> found = userRepository.findByUsername(user.getUsername());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }
}
