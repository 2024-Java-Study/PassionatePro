package com.example.pro.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(FormLoginConfigurer::disable);

        http.authorizeHttpRequests(request -> {
            request.requestMatchers(
                    antMatcher("/docs/**"),
                    antMatcher("/api/test"),
                    antMatcher("/health")
            ).permitAll();
        });

        return http.build();
    }
}
