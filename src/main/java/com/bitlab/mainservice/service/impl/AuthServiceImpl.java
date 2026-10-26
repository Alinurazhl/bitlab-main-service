package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.LoginRequest;
import com.bitlab.mainservice.dto.RefreshTokenRequest;
import com.bitlab.mainservice.dto.JwtResponse;
import com.bitlab.mainservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Processing login request for user: {}", loginRequest.getUsername());

        // URL токен-эндпоинта внутри сети Docker-Compose
        String keycloakUrl = "http://keycloak:8080/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Стандартные OAuth2 параметры для авторизации в Keycloak
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "password");
        bodyMap.add("client_id", "admin-cli");
        bodyMap.add("username", loginRequest.getUsername());
        bodyMap.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyMap, headers);

        try {
            @SuppressWarnings("unchecked")
            ResponseEntity<Map> response = restTemplate.postForEntity(keycloakUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                return new JwtResponse(
                        (String) responseBody.get("access_token"),
                        (String) responseBody.get("refresh_token"),
                        ((Number) responseBody.get("expires_in")).longValue(),
                        ((Number) responseBody.get("refresh_expires_in")).longValue(),
                        (String) responseBody.get("token_type")
                );
            }
        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            throw new IllegalArgumentException("Неверный логин или пароль");
        }

        throw new IllegalArgumentException("Не удалось получить токены от сервера авторизации");
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("Processing refresh token request");

        String keycloakUrl = "http://keycloak:8080/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // intensity.Формируем параметры для OAuth2 Refresh Token Grant
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "refresh_token");
        bodyMap.add("client_id", "admin-cli");
        bodyMap.add("refresh_token", refreshTokenRequest.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyMap, headers);

        try {
            @SuppressWarnings("unchecked")
            ResponseEntity<Map> response = restTemplate.postForEntity(keycloakUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                return new JwtResponse(
                        (String) responseBody.get("access_token"),
                        (String) responseBody.get("refresh_token"),
                        ((Number) responseBody.get("expires_in")).longValue(),
                        ((Number) responseBody.get("refresh_expires_in")).longValue(),
                        (String) responseBody.get("token_type")
                );
            }
        } catch (Exception e) {
            log.error("Refresh token failed: {}", e.getMessage());
            throw new IllegalArgumentException("Невалидный или истекший Refresh Token");
        }

        throw new IllegalArgumentException("Не удалось обновить токен");
    }
}
