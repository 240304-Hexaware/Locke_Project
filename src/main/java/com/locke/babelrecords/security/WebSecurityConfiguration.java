package com.locke.babelrecords.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChair(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests( auth -> auth
                    .requestMatchers("/api/v1/**")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
