package com.sidh.medinvoice.utils.prescription;

public class PrescriptionQueries {
    private PrescriptionQueries() {

    }
    public static final String INSERT_PRESCRIPTION = """
            INSERT INTO t_prs (a_prs_id, a_usr_id, a_prs_img_url, a_cr_dtm, a_upd_dtm, a_lat, a_long)
            VALUES (:prescriptionId, :userId, :prescriptionImgUrl, :createdDateTime, :updatedDateTime, :latitude, :longitude);
            """;
    public static final String FIND_PRESCRIPTION_BY_USER_ID = """
            SELECT a_prs_id, a_usr_id, a_prs_img_url, a_lat, a_long
            FROM t_prs WHERE a_usr_id = :userId;
            """;
    public static final String INSERT_MED_REPS_FOR_USER = """
            INSERT INTO t_usr_rep (a_usr_id, a_prs_id, a_rep_id)
            VALUES (:userId, :prescriptionId, :medicalRepId);
            """;
    public static final String FIND_PRESCRIPTION_BY_REP_ID = """
            SELECT p.a_prs_id, p.a_usr_id, p.a_prs_img_url, p.a_lat, p.a_long FROM t_prs p INNER JOIN t_usr_rep r
            ON p.a_prs_id = r.a_prs_id WHERE r.a_rep_id = :medicalRepId;
            """;
}
