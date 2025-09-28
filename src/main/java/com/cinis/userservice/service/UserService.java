package com.cinis.userservice.service;

import com.cinis.userservice.entity.User;

public interface UserService {
  User createUser(String name, String email, int age);
  User findUserById(Integer id);
  User updateUser(Integer id, String name, String email, Integer age);
  void deleteUser(Integer id);
}
