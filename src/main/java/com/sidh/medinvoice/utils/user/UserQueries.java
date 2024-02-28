package com.sidh.medinvoice.utils.user;

public class UserQueries {
    private UserQueries() {

    }

    public static final String INSERT_USER = """
            INSERT INTO t_usr (a_usr_id, a_em, a_pwd, a_fst_nm, a_lst_nm, a_role, a_cr_dtm, a_upd_dtm)
            VALUES (:userId, :email, :password, :firstName, :lastName, :role, :createdDateTime, :updatedDateTime);
            """;
    public static final String FIND_USER_BY_EMAIL_PASSWORD = """
            SELECT a_usr_id, a_em, a_pwd, a_fst_nm, a_lst_nm, a_role
            FROM t_usr WHERE a_em=:email;
            """;
}
