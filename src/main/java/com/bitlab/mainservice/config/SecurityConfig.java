package com.bitlab.mainservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**").permitAll()

                        // Требование LT15: Ограничиваем доступ к /course/** только для ADMIN
                        .requestMatchers(HttpMethod.POST, "/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/course/**").hasRole("ADMIN")

                        // Требование LT16: Ограничиваем доступ к /chapter/** только для ADMIN
                        .requestMatchers(HttpMethod.POST, "/chapter/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/chapter/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/chapter/**").hasRole("ADMIN")

                        // Требование LT17: Ограничиваем доступ к /lesson/** только для ADMIN
                        .requestMatchers(HttpMethod.POST, "/lesson/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lesson/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lesson/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}