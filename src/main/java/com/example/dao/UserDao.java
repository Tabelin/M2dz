package com.example.dao;

import com.example.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
    // Создать пользователя
    public User create(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created: {}", user);
            return user;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error creating user", e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    // Найти пользователя по ID
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("User found by ID {}: {}", id, user);
            } else {
                logger.warn("User not found by ID: {}", id);
            }
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            logger.error("Error finding user by ID: {}", id, e);
            throw new RuntimeException("Failed to find user by ID: " + id, e);
        }
    }

    // Обновить пользователя
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("User updated: {}", user);
            return user;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error updating user", e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    // Удалить пользователя
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                logger.info("User deleted with ID: {}", id);
            } else {
                logger.warn("User not found for deletion with ID: {}", id);
            }
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error deleting user with ID: {}", id, e);
            throw new RuntimeException("Failed to delete user with ID: " + id, e);
        }
    }

    // Получить всех пользователей
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Found {} users", users.size());
            return users;
        } catch (HibernateException e) {
            logger.error("Error fetching all users", e);
            throw new RuntimeException("Failed to fetch all users", e);
        }
    }
}