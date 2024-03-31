package com.sidh.medinvoice.utils.medicine;

public class MedicineQueries {
    private MedicineQueries() {

    }

    public static final String FIND_BY_USER_ID = """
            SELECT a_mdcn_id, a_usr_id, a_mdcn_nm, a_dsc, a_mrp, a_sp, a_qty
            FROM t_mdcn WHERE a_usr_id = :userId
            """;
}
