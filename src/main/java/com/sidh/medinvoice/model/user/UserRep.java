package com.sidh.medinvoice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRep implements Serializable {
    private String userId;
    private String prescriptionId;
    private String medicalRepId;
}
