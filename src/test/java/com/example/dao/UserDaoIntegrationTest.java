package com.example.dao;

import com.example.entity.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void setUp() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        userDao = new UserDao();
    }

    @Test
    void create_shouldPersistUser() {
        // Given
        User user = new User("Integration Test", "integration@test.com", 30);

        // When
        User saved = userDao.create(user);

        // Then
        assertNotNull(saved.getId());
        assertEquals("Integration Test", saved.getName());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void findById_shouldReturnUserWhenFound() {
        // Given
        User user = new User("Find Test", "find@test.com", 25);
        User saved = userDao.create(user);

        // When
        Optional<User> found = userDao.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Find Test", found.get().getName());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // When
        Optional<User> found = userDao.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void update_shouldUpdateUser() {
        // Given
        User user = new User("Update Test", "update@test.com", 28);
        User saved = userDao.create(user);
        saved.setName("Updated Name");
        saved.setAge(35);

        // When
        User updated = userDao.update(saved);

        // Then
        assertEquals("Updated Name", updated.getName());
        assertEquals(35, updated.getAge());
    }

    @Test
    void delete_shouldRemoveUser() {
        // Given
        User user = new User("Delete Test", "delete@test.com", 40);
        User saved = userDao.create(user);

        // When
        userDao.delete(saved.getId());

        // Then
        Optional<User> found = userDao.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Given
        userDao.create(new User("User 1", "user1@test.com", 20));
        userDao.create(new User("User 2", "user2@test.com", 25));

        // When
        List<User> allUsers = userDao.findAll();

        // Then
        assertNotNull(allUsers);
        assertTrue(allUsers.size() >= 2);
    }
}