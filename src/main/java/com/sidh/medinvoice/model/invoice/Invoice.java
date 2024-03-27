package com.sidh.medinvoice.model.invoice;

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
public class Invoice implements Serializable {
    private String invoiceId;
    private String userId;
    private String prescriptionId;
    private String invoiceNo;
    private String invoiceJson;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
