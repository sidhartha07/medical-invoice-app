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
public class MedicineInsertReqDto implements Serializable {
    private String userId;
    private String name;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private Integer quantity;
}
