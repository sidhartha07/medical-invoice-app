package com.sidh.medinvoice.model.medicine;

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
public class Medicine implements Serializable {
    private String medicineId;
    private String userId;
    private String name;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private Integer quantity;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
