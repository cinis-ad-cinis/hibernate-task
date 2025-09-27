package com.cinis.userservice.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  private static SessionFactory sessionFactory;

  public static synchronized SessionFactory getSessionFactory() {
    if (sessionFactory == null || sessionFactory.isClosed()) {
      sessionFactory = buildSessionFactory();
    }
    return sessionFactory;
  }

  private static SessionFactory buildSessionFactory() {
    try {
      StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").applySettings(System.getProperties()).build();
      Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
      return metadata.getSessionFactoryBuilder().build();
    } catch (Exception e) {
        System.err.println("Ошибка!" + e);
        throw new ExceptionInInitializerError(e);
    }
  }

  public static void close() {
    if (sessionFactory != null && !sessionFactory.isClosed()) {
      sessionFactory.close();
      sessionFactory = null;
    }
  }
}
