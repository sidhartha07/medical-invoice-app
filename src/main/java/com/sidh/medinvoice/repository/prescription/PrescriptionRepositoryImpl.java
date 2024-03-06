package com.sidh.medinvoice.repository.prescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.sidh.medinvoice.utils.prescription.PrescriptionCmnConstants.*;
import static com.sidh.medinvoice.utils.prescription.PrescriptionQueries.INSERT_PRESCRIPTION;

@Repository
public class PrescriptionRepositoryImpl implements PrescriptionRepository {
    private final Logger logger = LoggerFactory.getLogger(PrescriptionRepositoryImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void insert(String userId, String imageUrl) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        map.addValue(PRESCRIPTION_IMG_URL, imageUrl);
        map.addValue(CREATED_DATE_TIME, LocalDateTime.now());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        int inscnt = jdbcTemplate.update(INSERT_PRESCRIPTION, map);
        logger.info("insert to t_prs completed with {} count", inscnt);
    }
}
