package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.LoginRequest;
import com.bitlab.mainservice.dto.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
}
