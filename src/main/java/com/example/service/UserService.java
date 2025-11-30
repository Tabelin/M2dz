package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(User user) {
        return userDao.create(user);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public User updateUser(User user) {
        return userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }
}