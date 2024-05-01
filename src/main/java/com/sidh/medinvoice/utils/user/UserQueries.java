package com.sidh.medinvoice.utils.user;

public class UserQueries {
    private UserQueries() {

    }

    public static final String INSERT_USER = """
            INSERT INTO t_usr (a_usr_id, a_em, a_pwd, a_nm, a_role, a_cr_dtm, a_upd_dtm, a_lat, a_long, a_phn, a_shp_nm)
            VALUES (:userId, :email, :password, :fullName, :role, :createdDateTime, :updatedDateTime, :latitude, :longitude, :phoneNo, :shopName);
            """;
    public static final String FIND_USER_BY_EMAIL = """
            SELECT a_usr_id, a_em, a_pwd, a_nm, a_role, a_lat, a_long, a_phn, a_shp_nm
            FROM t_usr WHERE a_em = :email;
            """;
    public static final String FIND_USER_BY_PHONE_NO = """
            SELECT a_usr_id, a_em, a_pwd, a_nm, a_role, a_lat, a_long, a_phn, a_shp_nm
            FROM t_usr WHERE a_phn = :phoneNo;
            """;
    public static final String UPDATE_USER = """
            UPDATE t_usr
            SET a_em = :email, a_pwd = :password, a_nm = :fullName, a_phn = :phoneNo, a_shp_nm = :shopName, a_upd_dtm = :updatedDateTime
            WHERE a_usr_id = :userId;
            """;
    public static final String FIND_USER_BY_USER_ID = """
            SELECT a_usr_id, a_em, a_pwd, a_nm, a_role, a_lat, a_long, a_phn, a_shp_nm
            FROM t_usr WHERE a_usr_id = :userId;
            """;
    public static final String FIND_ALL_USERS = """
            SELECT a_usr_id, a_em, a_pwd, a_nm, a_role, a_lat, a_long, a_phn, a_shp_nm
            FROM t_usr;
            """;
    public static final String DELETE_USER = """
            DELETE FROM t_usr
            WHERE a_usr_id = :userId;
            """;
    public static final String UPDATE_CURRENT_LOCATION = """
            UPDATE t_usr
            SET a_cur_loc = :currentLocation, a_upd_dtm = :updatedDateTime
            WHERE a_usr_id = :userId;
            """;
    public static final String FIND_REPS_NEAR_USER = """
            SELECT a_usr_id, a_em, a_pwd, a_nm, a_role, a_lat, a_long, a_phn, a_shp_nm
            FROM t_usr
            WHERE a_role = 'REP'
            ORDER BY POWER(a_lat - :latitude, 2) + POWER(a_long - :longitude, 2)
            LIMIT 3;
            """;
}
