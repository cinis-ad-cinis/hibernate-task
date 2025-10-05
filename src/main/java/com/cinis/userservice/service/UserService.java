package com.cinis.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinis.userservice.dto.UserRequest;
import com.cinis.userservice.dto.UserResponse;
import com.cinis.userservice.entity.User;
import com.cinis.userservice.repository.UserRepository;


@Service
@Transactional
public class UserService {

  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserResponse createUser(UserRequest userRequest) {
    if (userRepository.existsByEmail(userRequest.getEmail())) {
      throw new RuntimeException("Email already exists: " + userRequest.getEmail());
    }

    User user = new User();
    user.setName(userRequest.getName());
    user.setEmail(userRequest.getEmail());
    user.setAge(userRequest.getAge());

    User savedUser = userRepository.save(user);
    return createResponse(savedUser);
  }

  @Transactional
  public UserResponse getUserById(int id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return createResponse(user);
  }

  public UserResponse updateUser(int id, UserRequest userRequest) {
    User existingUser = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    
    if (!existingUser.getEmail().equals(userRequest.getEmail()) && 
      userRepository.existsByEmail(userRequest.getEmail())) {
      throw new RuntimeException("Email already exists: " + userRequest.getEmail());
    }
    
    existingUser.setName(userRequest.getName());
    existingUser.setEmail(userRequest.getEmail());
    existingUser.setAge(userRequest.getAge());
    
    User updatedUser = userRepository.save(existingUser);
    return createResponse(updatedUser);
  }

  public void deleteUser(int id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  private UserResponse createResponse(User user) {
    return new UserResponse(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getAge(),
      user.getCreatedAt()
    );
  }
}
