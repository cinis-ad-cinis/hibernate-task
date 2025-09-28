package com.cinis.userservice.service;

import com.cinis.userservice.dao.UserDao;
import com.cinis.userservice.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  @Override
  public User createUser(String name, String email, int age) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setAge(age);
    userDao.create(user);
    return user;
  }

  @Override
  public User findUserById(Integer id) {
    return userDao.findById(id);
  }

  @Override
  public User updateUser(Integer id, String name, String email, Integer age) {
    User user = userDao.findById(id);
      if (user == null) {
          throw new RuntimeException("User with ID " + id + " not found");
      }
      
      if (name != null && !name.trim().isEmpty()) {
          user.setName(name);
      }
      if (email != null && !email.trim().isEmpty()) {
          user.setEmail(email);
      }
      if (age != null) {
          user.setAge(age);
      }
      
      userDao.update(user);
      return user;
    
  }

  @Override
  public void deleteUser(Integer id) {
    User user = userDao.findById(id);
    if (user == null) {
      throw new RuntimeException("User with ID " + id + " not found");
    }
    userDao.remove(user);
  }
}
