package com.sidh.medinvoice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto implements Serializable {
    private String email;
    private String password;
    private String fullName;
    private String role;
}
