package com.cinis.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotNull
  private int age;
}
