package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.request.LoginRequestDto;
import com.sidh.medinvoice.dto.request.RegisterRequestDto;
import com.sidh.medinvoice.dto.request.UpdateUserRequestDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.ResponseDto;
import com.sidh.medinvoice.dto.response.UserResponseDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.service.user.UserService;
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
    @ApiResponse(responseCode = "200", description = "Successful registration",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "200",
                      "message": "User registration success"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Registration failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "400",
                                "message": "Please provide mandatory fields"
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "Registration failed with Internal Server Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "500",
                                "message": "Email Id already exists, please try with another"
                            }
                            """)))
    public ResponseEntity<Object> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "Provide user details for registration",
            content = {@Content(schema = @Schema(name = "RegistrationRequestDto", example = """
                    {
                      "email": "user@email.com",
                      "password": "userpassword",
                      "fullName": "fullname",
                      "phoneNo": "phone",
                      "shopName": "shop",
                      "role": "userrole",
                      "currentLocation": "location"
                    }
                    """))}) @RequestBody RegisterRequestDto request) {
        if (!StringUtils.hasText(request.getEmail()) ||
                !StringUtils.hasText(request.getPassword()) ||
                !StringUtils.hasText(request.getFullName()) ||
                !StringUtils.hasText(request.getRole()) ||
                !StringUtils.hasText(request.getPhoneNo())) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        userService.createUser(request);
        MessageDto messageDto = MessageDto.builder()
                .status("200")
                .message("User registration success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful login",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": "200",
                        "message": "User logged in successfully",
                        "data": [
                          {
                            "userId": "2c8cf670-fc9e-4356-8049-90406a02b02b",
                            "email": "demo@email.com",
                            "fullName": "Demo User",
                            "phoneNo": "9999999999",
                            "shopName": "shop",
                            "role": "USER",
                            "currentLocation": ""
                          }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Login failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "400",
                                "message": "Please provide mandatory fields"
                            }
                            """)))
    @ApiResponse(responseCode = "401", description = "Login failed with authentication error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "401",
                                "message": "Invalid email or password"
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
                    .status("400")
                    .message("Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        UserResponseDto data = userService.userLogin(request);
        ResponseDto response = ResponseDto.builder()
                .status("200")
                .message("User logged in successfully")
                .data(List.of(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User profile updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "200",
                      "message": "User details updated successfully",
                      "data": [
                        {
                          "userId": "string",
                          "email": "string",
                          "fullName": "string",
                          "role": "string",
                          "currentLocation": "string"
                        }
                      ]
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Update failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "400",
                                "message": "Please provide mandatory fields"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Update failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "404",
                                "message": "No user found with this Id"
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "Update failed with Internal Server Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "500",
                                "message": "New Email already exists"
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
        UserResponseDto data = userService.userUpdate(request, userId);
        ResponseDto response = ResponseDto.builder()
                .status("200")
                .message("User details updated successfully")
                .data(List.of(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User fetched successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                       "status": "200",
                       "message": "User fetched successfully",
                       "data": [
                         {
                           "userId": "string",
                           "email": "string",
                           "fullName": "string",
                           "role": "string",
                           "currentLocation": "string"
                         }
                       ]
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "User fetch failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "400",
                                "message": "Please provide mandatory fields"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "User fetch failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "404",
                                "message": "No user found with this Id"
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "User fetch failed with Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "500",
                                "message": "Internal server error"
                            }
                            """)))
    public ResponseEntity<Object> findUserById(@PathVariable String userId) {
        if (!StringUtils.hasText(userId)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        UserResponseDto data = userService.findUserById(userId);
        ResponseDto response = ResponseDto.builder()
                .status("200")
                .message("User fetched successfully")
                .data(List.of(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Users fetched successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": "200",
                        "message": "Users fetched successfully",
                        "data": [
                          {
                            "userId": "string",
                            "email": "string",
                            "fullName": "string",
                            "role": "string",
                            "currentLocation": "string"
                          },
                          {
                            "userId": "string",
                            "email": "string",
                            "fullName": "string",
                            "role": "string",
                            "currentLocation": "string"
                          }
                       ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Fetch users failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "404",
                                "message": "No users found"
                            }
                            """)))
    public ResponseEntity<Object> findAllUser() {
        List<UserResponseDto> datas = userService.findAllUsers();
        ResponseDto response = ResponseDto.builder()
                .status("200")
                .message("Users fetched successfully")
                .data(datas)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/delete/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                       "status": "200",
                       "message": "User deleted successfully"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "User delete failed with bad request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "400",
                                "message": "Please provide mandatory fields"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "User delete failed with not found error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "404",
                                "message": "No user found with this Id"
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "User delete failed with Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                                "status": "500",
                                "message": "Internal server error"
                            }
                            """)))
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        if (!StringUtils.hasText(userId)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        userService.deleteUser(userId);
        MessageDto messageDto = MessageDto.builder()
                .status("200")
                .message("User deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<Object> handleUserExceptions(InvalidRequestException ex) {
        logger.warn("User operation failed: Error - {}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getMessageDto());
    }
}
