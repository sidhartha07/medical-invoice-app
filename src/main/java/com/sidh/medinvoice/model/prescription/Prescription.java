package com.sidh.medinvoice.model.prescription;

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
public class Prescription implements Serializable {
    private String prescriptionId;
    private String userId;
    private String prescriptionImgUrl;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private Double latitude;
    private Double longitude;
}
