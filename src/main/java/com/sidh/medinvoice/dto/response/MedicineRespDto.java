package com.sidh.medinvoice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineRespDto implements Serializable {
    private String status;
    private String message;
    private List<MedicineDto> medicines;
}
