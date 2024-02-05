package com.psu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean // This annotation tells Spring to use this method to create an instance of UserDetailsService.
    public UserDetailsService userDetailsService() {
        // Create an encoder that Spring Security uses for password hashing.
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Create an in-memory user detail service.
        var userDetailsService = new InMemoryUserDetailsManager();
        // Create a user (in a real system you would probably fetch the user data from a database).
        var user = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER").build(); // Assign user role. In a real system, roles can be used to manage permissions.
        userDetailsService.createUser(user); // Add the user to the service.
        return userDetailsService; // Return the service.
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/user/register", "/api/auth/login").permitAll() // Permit these endpoints
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                // Add your JWT filters here (JWT Authentication and Authorization filters)
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Additional beans (if any)
}


