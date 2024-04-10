package com.sidh.medinvoice.repository.medicine;

import com.sidh.medinvoice.mapper.medicine.MedicineRowMapper;
import com.sidh.medinvoice.model.medicine.Medicine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sidh.medinvoice.utils.medicine.MedicineCmnConstants.*;
import static com.sidh.medinvoice.utils.medicine.MedicineQueries.*;

@Repository
public class MedicineRepositoryImpl implements MedicineRepository {
    private static final Logger logger = LoggerFactory.getLogger(MedicineRepositoryImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Medicine> findMedicinesByUserId(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        List<Medicine> result = jdbcTemplate.query(FIND_BY_USER_ID, map, new MedicineRowMapper());
        logger.info("medicines fetched with count {}", result.size());
        return !CollectionUtils.isEmpty(result) ? result : null;
    }

    @Override
    public void addMedicine(Medicine medicine) {
        int inscnt = jdbcTemplate.update(INSERT_MEDICINE, getParams(medicine));
        logger.info("insert into t_mdcn completed with {} count", inscnt);
    }

    @Override
    public Medicine findMedicineByMedicineId(String medicineId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(MEDICINE_ID, medicineId);
        List<Medicine> result = jdbcTemplate.query(FIND_BY_MEDICINE_ID, map, new MedicineRowMapper());
        logger.info("medicine fetched with {} count", result.size());
        return !CollectionUtils.isEmpty(result) ? result.get(0) : null;
    }

    @Override
    public void updateMedicine(Medicine medicine) {
        int updcnt = jdbcTemplate.update(UPDATE_MEDICINE, getUpdateParams(medicine));
        logger.info("medicine with id {} updated with count {}", medicine.getMedicineId(), updcnt);
    }

    private MapSqlParameterSource getParams(Medicine medicine) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, medicine.getUserId());
        map.addValue(NAME, medicine.getName());
        map.addValue(DESCRIPTION, medicine.getDescription());
        map.addValue(MRP, medicine.getMrp());
        map.addValue(SELLING_PRICE, medicine.getSellingPrice());
        map.addValue(QUANTITY, medicine.getQuantity());
        map.addValue(CREATED_DATE_TIME, LocalDateTime.now());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        return map;
    }

    private MapSqlParameterSource getUpdateParams(Medicine medicine) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(MEDICINE_ID, medicine.getMedicineId());
        map.addValue(USER_ID, medicine.getUserId());
        map.addValue(NAME, medicine.getName());
        map.addValue(DESCRIPTION, medicine.getDescription());
        map.addValue(MRP, medicine.getMrp());
        map.addValue(SELLING_PRICE, medicine.getSellingPrice());
        map.addValue(QUANTITY, medicine.getQuantity());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        return map;
    }
}
