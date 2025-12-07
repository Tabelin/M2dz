package com.example.dao;

import com.example.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory testSessionFactory;
    private static UserDao userDao;

    @BeforeAll
    static void setUp() {

        Configuration configuration = new Configuration()
                .configure();

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());

        testSessionFactory = configuration.buildSessionFactory();

        userDao = new UserDaoImpl(testSessionFactory);
    }

    @AfterAll
    static void tearDown() {
        if (testSessionFactory != null) {
            testSessionFactory.close();
        }
    }

    @Test
    void testCreateUser() {
        User user = new User("Test User", "test@example.com", 30);
        User saved = userDao.create(user);

        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getName());
        assertEquals("test@example.com", saved.getEmail());
        assertEquals(30, saved.getAge());
    }

    @Test
    void testFindUserById() {
        User user = new User("Find Me", "find@example.com", 25);
        User saved = userDao.create(user);

        Optional<User> found = userDao.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Old Name", "old@example.com", 40);
        User saved = userDao.create(user);

        saved.setName("New Name");
        saved.setEmail("new@example.com");
        saved.setAge(45);

        User updated = userDao.update(saved);

        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(45, updated.getAge());
    }

    @Test
    void testDeleteUser() {
        User user = new User("ToDelete", "delete@example.com", 50);
        User saved = userDao.create(user);

        userDao.delete(saved.getId());

        Optional<User> found = userDao.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllUsers() {
        userDao.create(new User("User1", "user1@example.com", 20));
        userDao.create(new User("User2", "user2@example.com", 25));

        var users = userDao.findAll();

        assertTrue(users.size() >= 2);
    }
}