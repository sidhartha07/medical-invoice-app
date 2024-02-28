package com.sidh.medinvoice.repository;

import com.sidh.medinvoice.model.User;

public interface UserRepository {
    void create(User user);
    User login(String email);
}
