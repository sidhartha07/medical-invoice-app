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

import java.util.List;

import static com.sidh.medinvoice.utils.medicine.MedicineCmnConstants.USER_ID;
import static com.sidh.medinvoice.utils.medicine.MedicineQueries.FIND_BY_USER_ID;

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
}
