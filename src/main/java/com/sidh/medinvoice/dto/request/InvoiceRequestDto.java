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
public class InvoiceRequestDto implements Serializable {
    private String userId;
    private String prescriptionId;
    private String invoiceJson;
}
