package com.sidh.medinvoice.utils.invoice;

public class InvoiceQueries {
    private InvoiceQueries() {

    }

    public static final String FIND_INVOICE_BY_PRESCRIPTION_ID = """
            SELECT a_invc_id, a_usr_id, a_prs_id, a_inv_no, a_invc_json
            FROM t_invc WHERE a_prs_id = :prescriptionId;
            """;
    public static final String SAVE_INVOICE = """
            MERGE INTO t_invc as t1
            USING (SELECT :prescriptionId AS prescriptionId, :userId AS userId) AS t2
            ON t1.a_prs_id = t2.prescriptionId AND t1.a_usr_id = t2.userId
            WHEN MATCHED THEN
            UPDATE SET
                a_invc_json = :invoiceJson,
                a_upd_dtm = :updatedDateTime
            WHEN NOT MATCHED THEN
                INSERT (a_invc_id, a_usr_id, a_prs_id, a_inv_no, a_invc_json, a_cr_dtm, a_upd_dtm)
                VALUES (gen_random_uuid(), :userId, :prescriptionId, :invoiceNo, :invoiceJson, :createdDateTime, :updatedDateTime);
            """;
}
