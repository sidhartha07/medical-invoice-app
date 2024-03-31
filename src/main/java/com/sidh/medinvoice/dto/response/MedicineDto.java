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
public class MedicineDto implements Serializable {
    private String medicineId;
    private String userId;
    private String name;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private Integer quantity;
}
