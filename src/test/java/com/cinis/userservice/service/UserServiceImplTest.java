package com.cinis.userservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cinis.userservice.dao.UserDao;
import com.cinis.userservice.entity.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock
  private UserDao userDao;

  @InjectMocks
  private UserServiceImpl userService;

  private User testUser;
    
  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1);
    testUser.setName("Test Name");
    testUser.setEmail("name@example.com");
    testUser.setAge(32);
    testUser.setCreatedAt(LocalDateTime.now());
  }

  @Test
  void createUser_WithData_UserCreated() {
    String name = "Test Name";
    String email = "name@example.com";
    int age = 32; 
    
    User result = userService.createUser(name, email, age);

    assertNotNull(result);
    assertEquals(name, result.getName());
    assertEquals(email, result.getEmail());
    assertEquals(age, result.getAge());
    verify(userDao, times(1)).create(any(User.class));
  }

  @Test
  void getUserById_WithExistingId_UserReturned() {
    when(userDao.findById(1)).thenReturn(testUser);
    User result = userService.findUserById(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
    verify(userDao, times(1)).findById(1);
  }

  @Test
  void updateUser_WithData_UserUpdated() {
    when(userDao.findById(1)).thenReturn(testUser);

    User result = userService.updateUser(1, "New Name", "new@example.com", 35);

    assertNotNull(result);
    assertEquals("New Name", result.getName());
    assertEquals("new@example.com", result.getEmail());
    assertEquals(35, result.getAge());
    verify(userDao, times(1)).update(testUser);
  }

  @Test
  void updateUser_WithPartialData_FieldUpdated() {
    when(userDao.findById(1)).thenReturn(testUser);

    User result = userService.updateUser(1, "New Name", null, null);

    assertNotNull(result);
    assertEquals("New Name", result.getName());
    assertEquals("name@example.com", result.getEmail());
    assertEquals(32, result.getAge());
    verify(userDao, times(1)).update(testUser);
  }

  @Test
  void updateUser_WithNonExistentId_ExceptionThrown() {
    when(userDao.findById(9999)).thenReturn(null);

    try {
      userService.updateUser(9999, "New Name", "new@example.com", 32);
      fail("Expected RuntimeException was not thrown");
    } catch (RuntimeException e) {
      assertEquals("User with ID 9999 not found", e.getMessage());
    }
    verify(userDao, never()).update(any());
}

  @Test
  void deleteUser_WithExistingId_UserDeleted() {
    when(userDao.findById(1)).thenReturn(testUser);

    userService.deleteUser(1);

    verify(userDao, times(1)).findById(1);
    verify(userDao, times(1)).remove(testUser);
  }

  @Test
  void deleteUser_WithNonExistentId_ExceptionThrown() {
    when(userDao.findById(9999)).thenReturn(null);

    try {
      userService.deleteUser(9999);
      fail("Expected RuntimeException was not thrown");
    } catch (RuntimeException e) {
      assertEquals("User with ID 9999 not found", e.getMessage());
    }
    verify(userDao, never()).remove(any());
  }
}
