package com.cinis.userservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private int id;
  private String name;
  private String email;
  private int age;
  private LocalDateTime createdAt;
}
