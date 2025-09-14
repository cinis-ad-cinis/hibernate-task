package com.cinis.userservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
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
  private LocalDateTime createdAt;

  public User() {
    this.createdAt = LocalDateTime.now();
  }

  public User(String name, String email, Integer age) {
    this();
    this.name = name;
    this.email = email;
    this.age = age;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public LocalDateTime getCreatedAd() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "User{id= " + id + " name= " + name + " email=" + email + 
            " age= " + age + " createdAt= " + createdAt + "}";
  }
}
