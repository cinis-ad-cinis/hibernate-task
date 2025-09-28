package com.cinis.userservice.dao;

import org.hibernate.*;

import com.cinis.userservice.entity.User;
import com.cinis.userservice.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDaoImpl implements UserDao {
  
  @Override
  public void create(User user) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(user);
      session.flush();
      transaction.commit();
      log.info("User {} successfully saved with ID {}", user.getName(), user.getId());
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
        log.error("Error while saving user: {}", e.getMessage(), e);
        throw new RuntimeException("Failed to save user", e);
        
    }
  }

  @Override
  public User findById (Integer id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      User user = session.find(User.class, id);
      if (user == null) {
        log.warn("User with ID {} not found", id);
      }
      return user;
    } catch (Exception e) {
      log.error("Error while searching for user: {}", e.getMessage(), e);
      throw new RuntimeException("Could not find user with ID " + id, e);
    }
  }

  @Override
  public void update(User user) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();

      User existingUser = session.find(User.class, user.getId());
      if (existingUser == null) {
        throw new RuntimeException("User with ID " + user.getId() + " not found");
      }

      existingUser.setName(user.getName());
      existingUser.setEmail(user.getEmail());
      existingUser.setAge(user.getAge());

      session.merge(existingUser);

      transaction.commit();
      log.info("User {} with ID {} successfully updated", user.getName(), user.getId());
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      log.error("Error while updating user data {} with ID {}: {}", user.getName(), user.getId(), e.getMessage());
      throw new RuntimeException("Failed to update user with ID " + user.getId(), e);
    }
  }

  @Override 
  public void remove(User user) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      User existingUser = session.find(User.class, user.getId());
      if (existingUser != null) {
          session.remove(existingUser);
          transaction.commit();
          log.info("User with ID {} successfully removed", user.getId());
      } else {
        log.warn("User with ID {} does not exist", user.getId());
      } 
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      log.error("Error while trying to remove user: {}", e.getMessage());
      throw new RuntimeException("Failed to remove user with ID " + user.getId(), e);
    }
  }
}
