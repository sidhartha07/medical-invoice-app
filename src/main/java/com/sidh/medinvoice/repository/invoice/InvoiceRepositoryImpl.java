package com.sidh.medinvoice.repository.invoice;

import com.sidh.medinvoice.mapper.invoice.InvoiceRowMapper;
import com.sidh.medinvoice.model.invoice.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sidh.medinvoice.utils.invoice.InvoiceCmnConstants.*;
import static com.sidh.medinvoice.utils.invoice.InvoiceQueries.FIND_INVOICE_BY_PRESCRIPTION_ID;
import static com.sidh.medinvoice.utils.invoice.InvoiceQueries.SAVE_INVOICE;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final Logger logger = LoggerFactory.getLogger(InvoiceRepositoryImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Invoice> getInvoiceByPrescriptionId(String prescriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(PRESCRIPTION_ID, prescriptionId);
        List<Invoice> result = jdbcTemplate.query(FIND_INVOICE_BY_PRESCRIPTION_ID, map, new InvoiceRowMapper());
        logger.info("fetched invoices for prescriptionId {} with {} count", prescriptionId, result.size());
        return !CollectionUtils.isEmpty(result) ? result : null;
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        int inscnt = jdbcTemplate.update(SAVE_INVOICE, getParams(invoice));
        logger.info("Merge to t_invc completed with {} count", inscnt);
    }

    private MapSqlParameterSource getParams(Invoice invoice) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, invoice.getUserId());
        map.addValue(PRESCRIPTION_ID, invoice.getPrescriptionId());
        map.addValue(INVOICE_NO, invoice.getInvoiceNo());
        map.addValue(INVOICE_JSON, invoice.getInvoiceJson());
        map.addValue(CREATED_DATE_TIME, LocalDateTime.now());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        return map;
    }
}
