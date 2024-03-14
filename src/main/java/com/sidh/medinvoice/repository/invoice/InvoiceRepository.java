package com.sidh.medinvoice.repository.invoice;

import com.sidh.medinvoice.model.invoice.Invoice;

import java.util.List;

public interface InvoiceRepository {
    List<Invoice> getInvoiceByPrescriptionId(String prescriptionId);
}
