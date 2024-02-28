package com.sidh.medinvoice.service;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.response.LoginResponseDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.ResponseMsgDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.Role;
import com.sidh.medinvoice.model.User;
import com.sidh.medinvoice.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(RegisterRequestDto register) {
        String encryptedPwd = passwordEncoder.encode(register.getPassword());
        String role = register.getRole();
        if (role.equalsIgnoreCase("admin")) {
            role = Role.ADMIN.toString();
        } else if (role.equalsIgnoreCase("rep")) {
            role = Role.REP.toString();
        } else {
            role = Role.USER.toString();
        }
        User user = User.builder()
                .email(register.getEmail())
                .password(encryptedPwd)
                .fullName(register.getFullName())
                .role(role)
                .build();
        try {
            userRepository.create(user);
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10003")
                    .message("Email Id already exists, please try with another")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Registration Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, responseMsgDto);
        }
    }

    @Override
    public LoginResponseDto userLogin(LoginRequestDto request) {
        User user = userRepository.login(request.getEmail());
        if (ObjectUtils.isEmpty(user) || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10002")
                    .message("Invalid email or password")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Login Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.UNAUTHORIZED, responseMsgDto);
        }
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}
