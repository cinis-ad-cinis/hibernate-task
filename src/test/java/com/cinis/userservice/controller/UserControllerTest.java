package com.cinis.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cinis.userservice.dto.UserRequest;
import com.cinis.userservice.dto.UserResponse;
import com.cinis.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;
    
  @MockBean
  private UserService userService;


  @Test
  void createUser_Success() throws Exception {

    UserRequest userRequest = new UserRequest("Test Name", "name@example.com", 32);
    UserResponse userResponse = new UserResponse(1, "Test Name", "name@example.com", 32, LocalDateTime.now());

    when(userService.createUser(any())).thenReturn(userResponse);
    
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test Name"))
            .andExpect(jsonPath("$.email").value("name@example.com"))
            .andExpect(jsonPath("$.age").value(32));

    verify(userService, times(1)).createUser(any(UserRequest.class));
  }

  @Test
  void createUser_WithInvalidData_ReturnsBadRequest() throws Exception {
    UserRequest invalidRequest = new UserRequest("", "invalid-email", 54);

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());

    verify(userService, never()).createUser(any(UserRequest.class));
  }

  @Test
  void getUserById_WithExistingId_ReturnsUser() throws Exception {
      // Given
    UserResponse userResponse = new UserResponse(1, "Test Name", "name@example.com", 32, LocalDateTime.now());
    when(userService.getUserById(1)).thenReturn(userResponse);

    mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test Name"));

    verify(userService, times(1)).getUserById(1);
  }

  @Test
  void getUserById_WithNonExistingId_ReturnsNotFound() throws Exception {

    when(userService.getUserById(999)).thenThrow(new RuntimeException("User not found"));

    mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("User not found"));

    verify(userService, times(1)).getUserById(999);
  }

  @Test
  void updateUser_WithValidData_ReturnsUpdatedUser() throws Exception {

    UserRequest userRequest = new UserRequest("New Name", "new@example.com", 42);
    UserResponse userResponse = new UserResponse(1, "New Name", "new@example.com", 42, LocalDateTime.now());
    
    when(userService.updateUser(eq(1), any(UserRequest.class))).thenReturn(userResponse);

    mockMvc.perform(put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("New Name"))
            .andExpect(jsonPath("$.email").value("new@example.com"))
            .andExpect(jsonPath("$.age").value(42));
    
    verify(userService, times(1)).updateUser(eq(1), any(UserRequest.class));
  }

  @Test
  void deleteUser_WithExistingId_ReturnsNoContent() throws Exception {

    mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent());

    verify(userService, times(1)).deleteUser(1);
  }
}
