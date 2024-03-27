package com.sidh.medinvoice.utils.invoice;

public class InvoiceQueries {
    private InvoiceQueries() {

    }

    public static final String FIND_INVOICE_BY_PRESCRIPTION_ID = """
            SELECT a_invc_id, a_usr_id, a_prs_id, a_inv_no, a_invc_json
            FROM t_invc WHERE a_prs_id = :prescriptionId;
            """;
}
