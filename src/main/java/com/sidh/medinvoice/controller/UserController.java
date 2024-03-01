package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.request.UpdateUserRequestDto;
import com.sidh.medinvoice.dto.response.UserResponseDto;
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
                      "fullName": "fullname",
                      "role": "userrole"
                    }
                    """))}) @RequestBody RegisterRequestDto request) {
        if (!StringUtils.hasText(request.getEmail()) ||
                !StringUtils.hasText(request.getPassword()) ||
                !StringUtils.hasText(request.getFullName()) ||
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
                      "fullName": "fullname",
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
        UserResponseDto response = userService.userLogin(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User profile updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "userId": "c49c5cd9-9ca4-4a1e-91f4-b19b39ad8283",
                      "email": "user@email.com",
                      "fullName": "fullname",
                      "role": "USER"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Update failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "User update Failed with error",
                               "messages": [
                                 {
                                   "code": "10001",
                                   "message": "Please provide mandatory fields"
                                 }
                               ]
                             }
                            """)))
    @ApiResponse(responseCode = "404", description = "Update failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "exception": "User update Failed with error",
                              "messages": [
                                {
                                  "code": "10005",
                                  "message": "No user found with this Id"
                                }
                              ]
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "Update failed with Internal Server Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "User update Failed with error",
                               "messages": [
                                 {
                                   "code": "10004",
                                   "message": "Update failed with Internal server error"
                                 }
                               ]
                             }
                            """)))
    public ResponseEntity<Object> update(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "Provide user details to update. Only give the fields which needs to be updated.",
            content = {@Content(schema = @Schema(name = "UpdateUserRequestDto", example = """
                    {
                      "email": "user@email.com",
                      "password": "userpassword",
                      "fullName": "fullname"
                    }
                    """))}) @RequestBody UpdateUserRequestDto request, @PathVariable String userId) {
        UserResponseDto response = userService.userUpdate(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User fetched successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "userId": "c49c5cd9-9ca4-4a1e-91f4-b19b39ad8283",
                      "email": "user@email.com",
                      "fullName": "fullname",
                      "role": "USER"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "User fetch failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "User fetch Failed with error",
                               "messages": [
                                 {
                                   "code": "10001",
                                   "message": "Please provide mandatory fields"
                                 }
                               ]
                             }
                            """)))
    @ApiResponse(responseCode = "404", description = "User fetch failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "exception": "User fetch Failed with error",
                              "messages": [
                                {
                                  "code": "10005",
                                  "message": "No user found with this Id"
                                }
                              ]
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "User fetch failed with Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "exception": "User fetch Failed with error",
                                "messages": [
                                  {
                                    "code": "10004",
                                    "message": "Internal server error"
                                  }
                                ]
                              }
                            """)))
    public ResponseEntity<Object> findUserById(@PathVariable String userId) {
        if (!StringUtils.hasText(userId)) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10001")
                    .message("Please provide mandatory fields")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("User fetch Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        UserResponseDto response = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Users fetched successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    [
                       {
                         "userId": "8841ff07-9d11-44fb-b883-ecd3763f88ab",
                         "email": "newuser@email.com",
                         "fullName": "New User",
                         "role": "USER"
                       },
                       {
                         "userId": "c49c5cd9-9ca4-4a1e-91f4-b19b39ad8283",
                         "email": "adminuser@email.com",
                         "fullName": "admin user",
                         "role": "ADMIN"
                       }
                     ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Fetch users failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "exception": "Fetch users Failed with error",
                              "messages": [
                                {
                                  "code": "10005",
                                  "message": "No users found"
                                }
                              ]
                            }
                            """)))
    public ResponseEntity<Object> findAllUser() {
        List<UserResponseDto> response = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/delete/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                       "code": "200",
                       "message": "User deleted successfully"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "User delete failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                               "exception": "User delete Failed with error",
                               "messages": [
                                 {
                                   "code": "10001",
                                   "message": "Please provide mandatory fields"
                                 }
                               ]
                             }
                            """)))
    @ApiResponse(responseCode = "404", description = "User delete failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "exception": "User delete Failed with error",
                              "messages": [
                                {
                                  "code": "10005",
                                  "message": "No user found with this Id"
                                }
                              ]
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "User delete failed with Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "exception": "User delete Failed with error",
                                "messages": [
                                  {
                                    "code": "10004",
                                    "message": "Internal server error"
                                  }
                                ]
                              }
                            """)))
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        if (!StringUtils.hasText(userId)) {
            MessageDto messageDto = MessageDto.builder()
                    .code("10001")
                    .message("Please provide mandatory fields")
                    .build();
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("User delete Failed with error")
                    .messages(List.of(messageDto))
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        userService.deleteUser(userId);
        MessageDto messageDto = MessageDto.builder()
                .code("200")
                .message("User deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<Object> handleUserExceptions(InvalidRequestException ex) {
        ResponseMsgDto responseMsgDto = ex.getResponseMsgDto();
        logger.warn("User operation failed: Error - {}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMsgDto);
    }
}
