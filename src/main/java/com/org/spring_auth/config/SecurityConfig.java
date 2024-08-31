package com.org.spring_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static String[] PUBLIC_PATHS = new String[]{"/api/auth/public/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Disable CSRF since tokens are immune to it
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(PUBLIC_PATHS).permitAll() // These paths are allowed for everyone without authentication.
                                .anyRequest().authenticated() // Any other request must be authenticated.
                )
                // Set session management to stateless
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Disable form login and
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic Authentication - Will only enable REST access.
                // Imagine using "/main" through REST Client like Postman with Authorization -> Basic Auth -> username / password
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * @Bean annotation is used in Spring to indicate that the method will return a bean
     * that should be managed by the Spring container.
     * @return
     * The method returns an instance of AuthenticationProvider,
     * which is a core interface in Spring Security responsible for processing authentication requests.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        List<UserDetails> users = new ArrayList<>();

        for (String userName: Arrays.asList("a", "b")) {
            users.add(new User(userName, userName + "@123", List.of()));
        }

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        daoAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(users));

        return daoAuthenticationProvider;
    }
}
