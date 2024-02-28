package com.sidh.medinvoice.repository;

import com.sidh.medinvoice.mapper.user.UserRowMapper;
import com.sidh.medinvoice.model.User;
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
import static com.sidh.medinvoice.utils.user.UserQueries.FIND_USER_BY_EMAIL_PASSWORD;
import static com.sidh.medinvoice.utils.user.UserQueries.INSERT_USER;

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
        List<User> users = jdbcTemplate.query(FIND_USER_BY_EMAIL_PASSWORD, map, new UserRowMapper());
        logger.info("user fetched with {} count", users.size());
        return !CollectionUtils.isEmpty(users) ? users.get(0) : null;
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
        return new MapSqlParameterSource(map);
    }
}
