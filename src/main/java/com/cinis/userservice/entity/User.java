package com.cinis.userservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;
  
  @Column(name = "email", unique = true, nullable = false)
  private String email;
  
  @Column(name = "age", nullable = false)
  private int age;
  
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt =  LocalDateTime.now();
}
