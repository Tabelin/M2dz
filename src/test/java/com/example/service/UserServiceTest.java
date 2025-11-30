package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test Name", "test@example.com", 25);
        testUser.setId(1L);
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        // Given
        when(userDao.create(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.createUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userDao, times(1)).create(testUser);
    }

    @Test
    void findById_shouldReturnUserWhenFound() {
        // Given
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // Given
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findById(99L);

        // Then
        assertFalse(result.isPresent());
        verify(userDao, times(1)).findById(99L);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        // Given
        when(userDao.update(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userDao, times(1)).update(testUser);
    }

    @Test
    void deleteUser_shouldCallDaoDelete() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userDao, times(1)).delete(1L);
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Given
        when(userDao.findAll()).thenReturn(List.of(testUser));

        // When
        List<User> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userDao, times(1)).findAll();
    }
}