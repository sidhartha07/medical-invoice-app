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
public class UserResponseDto implements Serializable {
    private String userId;
    private String email;
    private String fullName;
    private String phoneNo;
    private String shopName;
    private String role;
    private String currentLocation;
}
