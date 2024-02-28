package com.sidh.medinvoice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto implements Serializable {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
