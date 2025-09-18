package com.cinis.userservice.dao;

import com.cinis.userservice.entity.User;

public interface UserDao {

    User findById(Integer id);

    void create(User user);

    void update(User user);
    
    void remove(User user);
}