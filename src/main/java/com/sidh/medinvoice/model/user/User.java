package com.sidh.medinvoice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private String phoneNo;
    private String shopName;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private Double latitude;
    private Double longitude;
}
