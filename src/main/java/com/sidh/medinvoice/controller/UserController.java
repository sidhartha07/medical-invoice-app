package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.response.LoginResponseDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.ResponseMsgDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Successful registration",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "code": "201",
                      "message": "User registration success"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Registration failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "exception": "Registration Failed with error",
                              "messages": [
                                {
                                  "code": "10001",
                                  "message": "Please provide mandatory fields"
                                }
                              ]
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "Registration failed with Internal Server Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "Registration Failed with error",
                               "messages": [
                                 {
                                   "code": "10003",
                                   "message": "Email Id already exists, please try with another"
                                 }
                               ]
                             }
                            """)))
    public ResponseEntity<Object> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "Provide user details for registration",
            content = {@Content(schema = @Schema(name = "RegistrationRequestDto", example = """
                    {
                      "email": "user@email.com",
                      "password": "userpassword",
                      "firstName": "firstname",
                      "lastName": "lastname",
                      "role": "userrole"
                    }
                    """))}) @RequestBody RegisterRequestDto request) {
        if (!StringUtils.hasText(request.getEmail()) ||
                !StringUtils.hasText(request.getPassword()) ||
                !StringUtils.hasText(request.getFirstName()) ||
                !StringUtils.hasText(request.getLastName()) ||
                !StringUtils.hasText(request.getRole())) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10001")
                    .message("Please provide mandatory fields")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Registration Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        userService.createUser(request);
        MessageDto messageDto = MessageDto.builder()
                .code("201")
                .message("User registration success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful login",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "userId": "c49c5cd9-9ca4-4a1e-91f4-b19b39ad8283",
                      "email": "user@email.com",
                      "firstName": "firstname",
                      "lastName": "lastname",
                      "role": "USER"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Login failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "Login Failed with error",
                               "messages": [
                                 {
                                   "code": "10001",
                                   "message": "Please provide mandatory fields"
                                 }
                               ]
                             }
                            """)))
    @ApiResponse(responseCode = "401", description = "Login failed with authentication error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "exception": "Login Failed with error",
                                "messages": [
                                  {
                                    "code": "10002",
                                    "message": "Invalid email or password"
                                  }
                                ]
                              }
                            """)))
    public ResponseEntity<Object> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "Provide email and password to login",
            content = {@Content(schema = @Schema(name = "LoginRequestDto", example = """
                    {
                      "email": "user@email.com",
                      "password": "userpassword"
                    }
                    """))}) @RequestBody LoginRequestDto request) {
        if (!StringUtils.hasText(request.getEmail()) ||
                !StringUtils.hasText(request.getPassword())) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10001")
                    .message("Please provide mandatory fields")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Login Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        LoginResponseDto response = userService.userLogin(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<Object> handleAuthFailure(InvalidRequestException ex) {
        ResponseMsgDto responseMsgDto = ex.getResponseMsgDto();
        logger.warn("Login/Register failed: Error - {}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMsgDto);
    }
}
