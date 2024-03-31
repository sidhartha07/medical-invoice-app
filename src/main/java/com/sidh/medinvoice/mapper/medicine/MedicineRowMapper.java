package com.sidh.medinvoice.mapper.medicine;

import com.sidh.medinvoice.model.medicine.Medicine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicineRowMapper implements RowMapper<Medicine> {
    @Override
    public Medicine mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Medicine.builder()
                .medicineId(rs.getString("a_mdcn_id"))
                .userId(rs.getString("a_usr_id"))
                .name(rs.getString("a_mdcn_nm"))
                .description(rs.getString("a_dsc"))
                .mrp(rs.getDouble("a_mrp"))
                .sellingPrice(rs.getDouble("a_sp"))
                .quantity(rs.getInt("a_qty"))
                .build();
    }
}
