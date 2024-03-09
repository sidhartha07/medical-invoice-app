package com.sidh.medinvoice.service.user;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.request.UpdateUserRequestDto;
import com.sidh.medinvoice.dto.response.UserResponseDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.ResponseMsgDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.user.Role;
import com.sidh.medinvoice.model.user.User;
import com.sidh.medinvoice.repository.user.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(RegisterRequestDto register) {
        String encryptedPwd = passwordEncoder.encode(register.getPassword());
        String role = register.getRole();
        String[] geoValues = register.getCurrentLocation().split(",");
        Double latitude = Double.valueOf(geoValues[0]);
        Double longitude = Double.valueOf(geoValues[1]);
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
                .latitude(latitude)
                .longitude(longitude)
                .build();
        try {
            userRepository.create(user);
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("500")
                    .message("Email Id already exists, please try with another")
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
        }
    }

    @Override
    public UserResponseDto userLogin(LoginRequestDto request) {
        User user = userRepository.login(request.getEmail());
        if (ObjectUtils.isEmpty(user) || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            MessageDto messageDto = MessageDto.builder()
                    .status("401")
                    .message("Invalid email or password")
                    .build();
            throw new InvalidRequestException(HttpStatus.UNAUTHORIZED, messageDto);
        }
        return mapUserToRespDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto userUpdate(UpdateUserRequestDto request, String userId) {
        User user = User.builder().build();
        if (StringUtils.hasText(userId)) {
            user = userRepository.findByUserId(userId);
        } else {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        if (ObjectUtils.isEmpty(user)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("No user found with this Id")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (StringUtils.hasText(request.getFullName())) {
            user.setFullName(request.getFullName());
        }
        try {
            userRepository.update(user);
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("500")
                    .message("New Email already exists")
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
        }
        return mapUserToRespDto(user);
    }

    @Override
    public UserResponseDto findUserById(String userId) {
        User user = User.builder().build();
        try {
            user = userRepository.findByUserId(userId);
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("500")
                    .message("Internal server error")
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
        }
        if (ObjectUtils.isEmpty(user)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("No user found with this Id")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        return mapUserToRespDto(user);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
            if (CollectionUtils.isEmpty(users)) {
                throw new RuntimeException();
            }
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("No users found")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user : users) {
            UserResponseDto userResponseDto = mapUserToRespDto(user);
            userResponseDtos.add(userResponseDto);
        }
        return userResponseDtos;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        int dltcnt = 0;
        try {
            dltcnt = userRepository.delete(userId);

        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("500")
                    .message("Internal server error")
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
        }
        if (dltcnt <= 0) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("No user found with this Id")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
    }

    private static UserResponseDto mapUserToRespDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}
