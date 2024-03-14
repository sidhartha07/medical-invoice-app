package com.sidh.medinvoice.repository.user;

import com.sidh.medinvoice.mapper.user.UserRowMapper;
import com.sidh.medinvoice.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sidh.medinvoice.utils.user.UserCmnConstants.*;
import static com.sidh.medinvoice.utils.user.UserQueries.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void create(User user) {
        user.setUserId(UUID.randomUUID().toString());
        MapSqlParameterSource map = getSqlParameterSource(user);
        String query = INSERT_USER;
        if(StringUtils.hasText(query) && !ObjectUtils.isEmpty(user)) {
            int inscnt = jdbcTemplate.update(query, map);
            logger.info("insert to t_usr completed with {} count", inscnt);
        } else {
            logger.info("insert to t_usr completed with empty count");
        }
    }

    @Override
    public User login(String email) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(EMAIL, email);
        List<User> users = jdbcTemplate.query(FIND_USER_BY_EMAIL, map, new UserRowMapper());
        logger.info("user fetched with {} count", users.size());
        return !CollectionUtils.isEmpty(users) ? users.get(0) : null;
    }

    @Override
    public void update(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, user.getUserId());
        map.addValue(EMAIL, user.getEmail());
        map.addValue(PASSWORD, user.getPassword());
        map.addValue(FULL_NAME, user.getFullName());
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        String query = UPDATE_USER;
        if(StringUtils.hasText(query) && !ObjectUtils.isEmpty(user)) {
            int inscnt = jdbcTemplate.update(query, map);
            logger.info("update to t_usr completed with {} count", inscnt);
        } else {
            logger.info("update to t_usr completed with empty count");
        }
    }

    @Override
    public User findByUserId(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        List<User> users = jdbcTemplate.query(FIND_USER_BY_USER_ID, map, new UserRowMapper());
        logger.info("user with id {} fetched with {} count", userId, users.size());
        return !CollectionUtils.isEmpty(users) ? users.get(0) : null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper());
        logger.info("users fetched with {} count", users.size());
        return !CollectionUtils.isEmpty(users) ? users : null;
    }

    @Override
    public int delete(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        int dltcnt = jdbcTemplate.update(DELETE_USER, map);
        logger.info("user deleted with {} count", dltcnt);
        return dltcnt;
    }

    public int updateCurrentLocation(String userId, String currentLocation) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(USER_ID, userId);
        map.addValue(CURRENT_LOCATION, currentLocation);
        map.addValue(UPDATED_DATE_TIME, LocalDateTime.now());
        int updcnt = jdbcTemplate.update(UPDATE_CURRENT_LOCATION, map);
        logger.info("current location for user with userId {} updated with {} count", userId, updcnt);
        return updcnt;
    }

    @Override
    public List<User> findRepsNearUser(Double latitude, Double longitude) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(LATITUDE, latitude);
        map.addValue(LONGITUDE, longitude);
        List<User> result = jdbcTemplate.query(FIND_REPS_NEAR_USER, map, new UserRowMapper());
        return !CollectionUtils.isEmpty(result) ? result : null;
    }

    private MapSqlParameterSource getSqlParameterSource(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, user.getUserId());
        map.put(EMAIL, user.getEmail());
        map.put(PASSWORD, user.getPassword());
        map.put(FULL_NAME, user.getFullName());
        map.put(ROLE, user.getRole());
        map.put(CREATED_DATE_TIME, LocalDateTime.now());
        map.put(UPDATED_DATE_TIME, LocalDateTime.now());
        map.put(LATITUDE, user.getLatitude());
        map.put(LONGITUDE, user.getLongitude());
        return new MapSqlParameterSource(map);
    }
}
