package com.sidh.medinvoice.repository.user;

import com.sidh.medinvoice.model.user.User;

import java.util.List;

public interface UserRepository {
    void create(User user);
    User login(String email);
    void update(User user);
    User findByUserId(String userId);
    List<User> findAll();
    int delete(String userId);
    int updateCurrentLocation(String userId, String currentLocation);
    List<User> findRepsNearUser(Double latitude, Double longitude);
}
