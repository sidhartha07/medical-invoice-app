package com.sidh.medinvoice.mapper.prescription;

import com.sidh.medinvoice.model.prescription.Prescription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescriptionRowMapper implements RowMapper<Prescription> {
    @Override
    public Prescription mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Prescription.builder()
                .prescriptionId(rs.getString("a_prs_id"))
                .userId(rs.getString("a_usr_id"))
                .prescriptionImgUrl(rs.getString("a_prs_img_url"))
                .latitude(rs.getDouble("a_lat"))
                .longitude(rs.getDouble("a_long"))
                .build();
    }
}
