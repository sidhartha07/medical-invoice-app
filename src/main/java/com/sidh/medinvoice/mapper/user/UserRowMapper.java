package com.sidh.medinvoice.mapper.user;

import com.sidh.medinvoice.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .userId(rs.getString("a_usr_id"))
                .email(rs.getString("a_em"))
                .password(rs.getString("a_pwd"))
                .fullName(rs.getString("a_nm"))
                .role(rs.getString("a_role"))
                .latitude(rs.getDouble("a_lat"))
                .longitude(rs.getDouble("a_long"))
                .build();
    }
}
