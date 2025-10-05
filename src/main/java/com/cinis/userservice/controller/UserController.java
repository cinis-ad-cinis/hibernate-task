package com.cinis.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinis.userservice.dto.UserRequest;
import com.cinis.userservice.dto.UserResponse;
import com.cinis.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }
    
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable("id") int id) {
    UserResponse userResponse = userService.getUserById(id);
    return ResponseEntity.ok(userResponse);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") int id, 
      @Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.updateUser(id, userRequest);
    return ResponseEntity.ok(userResponse);
  }
    
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
    
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
