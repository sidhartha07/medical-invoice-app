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
public class PrescriptionDto implements Serializable {
    private String prescriptionId;
    private String imageUrl;
    private List<InvoiceDto> invoices;
}
