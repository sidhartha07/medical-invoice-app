package com.sidh.medinvoice.service.user;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.request.UpdateUserRequestDto;
import com.sidh.medinvoice.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    void createUser(RegisterRequestDto register);
    UserResponseDto userLogin(LoginRequestDto request);
    UserResponseDto userUpdate(UpdateUserRequestDto request, String userId);
    UserResponseDto findUserById(String userId);
    List<UserResponseDto> findAllUsers();
    void deleteUser(String userId);
}
