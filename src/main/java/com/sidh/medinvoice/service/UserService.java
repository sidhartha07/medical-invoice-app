package com.sidh.medinvoice.service;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.response.LoginResponseDto;

public interface UserService {
    void createUser(RegisterRequestDto register);
    LoginResponseDto userLogin(LoginRequestDto request);
}
