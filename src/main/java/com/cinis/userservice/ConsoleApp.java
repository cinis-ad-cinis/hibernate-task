package com.cinis.userservice;

import java.util.Scanner;

import com.cinis.userservice.dao.UserDao;
import com.cinis.userservice.dao.UserDaoImpl;
import com.cinis.userservice.entity.User;
import com.cinis.userservice.service.UserService;
import com.cinis.userservice.service.UserServiceImpl;
import com.cinis.userservice.utils.HibernateUtil;

public class ConsoleApp {
  private static final UserDao userDao = new UserDaoImpl();
  private static final UserService userService = new UserServiceImpl(userDao);
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    try {
        System.out.println("=== User Service ===");
        start();
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      } finally {
        HibernateUtil.close();
        scanner.close();
      }
  }

  private static void start() {
    boolean starting = true;
    while(starting) {
      printMenu();
      String value = scanner.nextLine();

      switch (value) {
        case "1": createUser(); break;
        case "2": findUserById(); break;
        case "3": updateUser(); break;
        case "4": deleteUser(); break;
        case "5": starting = false; break;
        default: System.out.println("Invalid value.");
      }
    }
    System.out.println("Application closed.");
  }

  private static void createUser() {
    System.out.print("Enter name: ");
    String name = scanner.nextLine();
    
    System.out.print("Enter email: ");
    String email = scanner.nextLine();
    
    System.out.print("Enter age: ");
    int age = Integer.parseInt(scanner.nextLine());

    try {
      User user = userService.createUser(name, email, age);
      System.out.println("User created successfully with ID: " + user.getId());
    } catch (Exception e) {
      System.out.println("Error creating user: " + e.getMessage());
    }
  }

  private static void findUserById() {
    System.out.print("Enter user ID: ");
    int id = Integer.parseInt(scanner.nextLine());
    
    try {
      User user = userService.findUserById(id);
      System.out.println(user != null ? user : "User with ID " + id + " not found.");
    } catch (Exception e) {
      System.out.println("Error finding user: " + e.getMessage());
    }
  }

  private static void updateUser() {
    System.out.print("Enter user ID to update: ");
    int id = Integer.parseInt(scanner.nextLine());
    
    try {
      User user = userService.findUserById(id);
      if (user == null) {
        System.out.println("User with ID " + id + " not found.");
        return;
      }

      System.out.println("Current user details:");
      System.out.println("Name: " + user.getName());
      System.out.println("Email: " + user.getEmail());
      System.out.println("Age: " + user.getAge());

      System.out.println("\nEnter new details:");

      System.out.print("New name: ");
      String newName = scanner.nextLine();

      System.out.print("New email: ");
      String newEmail = scanner.nextLine();
      
      System.out.print("New age: ");
      String newAgeStr = scanner.nextLine();
      Integer newAge = newAgeStr.trim().isEmpty() ? null : Integer.parseInt(newAgeStr);

      User updatedUser = userService.updateUser(id, 
        newName.trim().isEmpty() ? null : newName,
        newEmail.trim().isEmpty() ? null : newEmail,
        newAge);

      System.out.println("User updated successfully: " + updatedUser);
    } catch (Exception e) {
        System.out.println("Error updating user: " + e.getMessage());
    }
  }

  private static void deleteUser() {
    System.out.print("Enter user ID to delete: ");
    int id = Integer.parseInt(scanner.nextLine());
    
    try {
      User user = userService.findUserById(id);
      if (user == null) {
        System.out.println("User with ID " + id + " not found.");
        return;
      }
      userService.deleteUser(id);
    } catch (Exception e) {
      System.out.println("Error deleting user: " + e.getMessage());
    }
  }

  private static void printMenu() {
    System.out.println("Enter a command :\n" + 
      "1. Create user\n" +
      "2. Find user by ID\n" + 
      "3. Update user\n" +
      "4. Delete user\n" +
      "5. Exit\n");
  }
}