package com.sidh.medinvoice.utils.prescription;

public class PrescriptionQueries {
    private PrescriptionQueries() {

    }
    public static final String INSERT_PRESCRIPTION = """
            INSERT INTO t_prs (a_prs_id, a_usr_id, a_prs_img_url, a_cr_dtm, a_upd_dtm)
            VALUES (gen_random_uuid(), :userId, :prescriptionImgUrl, :createdDateTime, :updatedDateTime);
            """;
}
