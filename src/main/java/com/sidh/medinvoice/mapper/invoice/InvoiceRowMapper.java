package com.sidh.medinvoice.mapper.invoice;

import com.sidh.medinvoice.model.invoice.Invoice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceRowMapper implements RowMapper<Invoice> {
    @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Invoice.builder()
                .invoiceId(rs.getString("a_invc_id"))
                .userId(rs.getString("a_usr_id"))
                .prescriptionId(rs.getString("a_prs_id"))
                .invoiceNo(rs.getString("a_inv_no"))
                .invoiceJson(rs.getString("a_invc_json"))
                .build();
    }
}
