package com.sidh.medinvoice.utils.medicine;

public class MedicineQueries {
    private MedicineQueries() {

    }

    public static final String FIND_BY_USER_ID = """
            SELECT a_mdcn_id, a_usr_id, a_mdcn_nm, a_dsc, a_mrp, a_sp, a_qty
            FROM t_mdcn WHERE a_usr_id = :userId
            """;
    public static final String INSERT_MEDICINE = """
            INSERT INTO t_mdcn (a_mdcn_id, a_usr_id, a_mdcn_nm, a_dsc, a_mrp, a_sp, a_qty, a_cr_dtm, a_upd_dtm)
            VALUES (gen_random_uuid(), :userId, :name, :description, :mrp, :sellingPrice, :quantity, :createdDateTime, :updatedDateTime);
            """;
    public static final String FIND_BY_MEDICINE_ID = """
            SELECT a_mdcn_id, a_usr_id, a_mdcn_nm, a_dsc, a_mrp, a_sp, a_qty
            FROM t_mdcn WHERE a_mdcn_id = :medicineId
            """;
    public static final String UPDATE_MEDICINE = """
            UPDATE t_mdcn
            SET a_usr_id = :userId, a_mdcn_nm = :name, a_dsc = :description, a_mrp = :mrp, a_sp = :sellingPrice, a_qty = :quantity, a_upd_dtm = :updatedDateTime
            WHERE a_mdcn_id = :medicineId;
            """;
}
