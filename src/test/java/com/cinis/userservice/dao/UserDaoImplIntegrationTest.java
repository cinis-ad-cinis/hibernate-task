package com.cinis.userservice.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import com.cinis.userservice.entity.User;
import com.cinis.userservice.utils.HibernateUtil;


@Testcontainers
public class UserDaoImplIntegrationTest {
  
  @Container
  private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
    .withDatabaseName("testdb")
    .withUsername("testuser")
    .withPassword("testpass");

  private UserDaoImpl userDao;

  @BeforeAll
  static void beforeAll() {

    System.setProperty("hibernate.connection.url", database.getJdbcUrl());
    System.setProperty("hibernate.connection.username", database.getUsername());
    System.setProperty("hibernate.connection.password", database.getPassword());
    System.setProperty("hibernate.hbm2ddl.auto", "create-drop");

    HibernateUtil.getSessionFactory();
  }

  @BeforeEach
  void setUp() {
    userDao = new UserDaoImpl();
  }

  @AfterEach
  void clearUsersTable() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction transaction = session.beginTransaction();
      session.createMutationQuery("DELETE FROM User").executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
    }
  }

    @AfterAll
    static void afterAll() {
        HibernateUtil.close();
    }

    private User createUser(String name, String email, int age) {
      User user = new User();
      user.setName(name);
      user.setEmail(email);
      user.setAge(age);
      return user;
    }

    private User createSavedUser(String name, String email, int age) {
      User user = createUser(name, email, age);
      userDao.create(user);
      return user;
    }

    @Test
    void createUser_WithData_UserCreated() {
      User user = createUser("Test Name", "name@example.com", 32);

      userDao.create(user);

      assertTrue(user.getId() > 0);
      
      User result = userDao.findById(user.getId());
      assertNotNull(result);
      assertEquals("Test Name", result.getName());
      assertEquals("name@example.com", result.getEmail());
      assertEquals(32, result.getAge());
      assertNotNull(result.getCreatedAt());
    }

    @Test
    void createUser_WithDuplicateEmail_ExceptionThrown() {
      User user1 = createUser("Test Name", "samemail@example.com", 32);
      userDao.create(user1);

      User user2 = createUser("Other Name", "samemail@example.com", 23);

      assertThrows(RuntimeException.class, () -> userDao.create(user2));
    }

    @Test
    void findById_WithExistingId_UserReturned() {
      User user = createSavedUser("Test Name", "name@example.com", 32);

      User result = userDao.findById(user.getId());

      assertNotNull(result);
      assertEquals(user.getId(), result.getId());
      assertEquals("Test Name", result.getName());
    }

    @Test
    void findById_WithNonExistentId_NullReturned() {
      User result = userDao.findById(9999);

      assertNull(result);
    }

    @Test
    void updateUser_WithValidData_UserUpdated() {
      User user = createSavedUser("Test Name", "name@example.com", 32);

      user.setName("New Name");
      user.setEmail("new@example.com");
      user.setAge(35);
      userDao.update(user);

      User updatedUser = userDao.findById(user.getId());
      assertEquals("New Name", updatedUser.getName());
      assertEquals("new@example.com", updatedUser.getEmail());
      assertEquals(35, updatedUser.getAge());
    }

    @Test
    void updateUser_WithNonExistentUser_ExceptionThrown() {
      User user = createUser("Test Name", "name@example.com", 32);
      user.setId(9999);

      assertThrows(RuntimeException.class, () -> userDao.update(user));
  }

    @Test
    void remove_WithExistingUser_UserDeleted() {
      User user = createSavedUser("Test Name", "name@example.com", 32);

      userDao.remove(user);

      User result = userDao.findById(user.getId());
      assertNull(result);
    }

    @Test
    void remove_WithNonExistentUser_NoExceptionThrown() {
      User user = createUser("Test Name", "name@example.com", 32);
      user.setId(9999);

      userDao.remove(user);

      User result = userDao.findById(9999);
      assertNull(result);
    }
}
