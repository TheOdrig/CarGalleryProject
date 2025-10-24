package com.akif.service;

import com.akif.dto.request.LoginRequestDto;
import com.akif.dto.request.RefreshTokenRequestDto;
import com.akif.dto.request.RegisterRequestDto;
import com.akif.dto.response.AuthResponseDto;

public interface IAuthService {

    AuthResponseDto register(RegisterRequestDto registerRequest);

    AuthResponseDto login(LoginRequestDto loginRequest);

    AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest);
}