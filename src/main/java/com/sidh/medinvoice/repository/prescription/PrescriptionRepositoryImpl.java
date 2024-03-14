package com.sidh.medinvoice.repository.prescription;

import com.sidh.medinvoice.mapper.prescription.PrescriptionRowMapper;
import com.sidh.medinvoice.model.prescription.Prescription;
import com.sidh.medinvoice.model.user.UserRep;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static com.sidh.medinvoice.utils.prescription.PrescriptionCmnConstants.*;
import static com.sidh.medinvoice.utils.prescription.PrescriptionQueries.*;

@Repository
public class PrescriptionRepositoryImpl implements PrescriptionRepository {
    private final Logger logger = LoggerFactory.getLogger(PrescriptionRepositoryImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public String insert(String userId, String imageUrl, Double latitude, Double longitude) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        String prescriptionId = UUID.randomUUID().toString();
        map.addValue(PRESCRIPTION_ID, prescriptionId);
        map.addValue(USER_ID, userId);
        map.addValue(PRESCRIPTION_IMG_URL, imageUrl);
        map.addValue(CREATED_DATE_TIME, LocalDateTime.now());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        map.addValue(LATITUDE, latitude);
        map.addValue(LONGITUDE, longitude);
        int inscnt = jdbcTemplate.update(INSERT_PRESCRIPTION, map);
        logger.info("insert to t_prs completed with {} count", inscnt);
        return prescriptionId;
    }

    @Override
    public List<Prescription> getPrescriptionByUserId(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        List<Prescription> result = jdbcTemplate.query(FIND_PRESCRIPTION_BY_USER_ID, map, new PrescriptionRowMapper());
        logger.info("fetched prescriptions for userId {} with {} count", userId, result.size());
        return !CollectionUtils.isEmpty(result) ? result : null;
    }

    @Override
    public int saveRepsForUser(List<UserRep> reps) {
        int[] count = jdbcTemplate.batchUpdate(INSERT_MED_REPS_FOR_USER, getSqlParameterSource(reps));
        if (!ArrayUtils.isEmpty(count)) {
            logger.info("inserted {} rows to t_usr_rep", count.length);
        }
        return !ArrayUtils.isEmpty(count) ? count.length : 0;
    }

    private SqlParameterSource[] getSqlParameterSource(List<UserRep> reps) {
        return reps.stream().map(rep -> {
            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue(USER_ID, rep.getUserId());
            map.addValue(PRESCRIPTION_ID, rep.getPrescriptionId());
            map.addValue(MEDICAL_REP_ID, rep.getMedicalRepId());
            return map;
        }).toArray(SqlParameterSource[]::new);
    }

    private static class UserRepMapper implements RowMapper<UserRep> {

        @Override
        public UserRep mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserRep.builder()
                    .userId(rs.getString("a_usr_id"))
                    .prescriptionId(rs.getString("a_prs_id"))
                    .medicalRepId(rs.getString("a_rep_id"))
                    .build();
        }
    }
}
