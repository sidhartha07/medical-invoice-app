package com.sidh.medinvoice.utils.invoice;

public class InvoiceQueries {
    private InvoiceQueries() {

    }

    public static final String FIND_INVOICE_BY_PRESCRIPTION_ID = """
            SELECT a_invc_id, a_usr_id, a_prs_id, a_inv_no, a_invc_json
            FROM t_invc WHERE a_prs_id = :prescriptionId;
            """;
    public static final String SAVE_INVOICE = """
            INSERT INTO t_invc (a_invc_id, a_usr_id, a_prs_id, a_inv_no, a_invc_json, a_cr_dtm, a_upd_dtm)
            VALUES (gen_random_uuid(), :userId, :prescriptionId, :invoiceNo, :invoiceJson, :createdDateTime, :updatedDateTime);
            """;
}
