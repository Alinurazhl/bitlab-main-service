package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.LoginRequest;
import com.bitlab.mainservice.dto.RefreshTokenRequest;
import com.bitlab.mainservice.dto.JwtResponse;
import com.bitlab.mainservice.dto.UserRegistrationDto;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void registerUser(UserRegistrationDto registrationDto);
}
