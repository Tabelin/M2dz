package com.example.dao;

import com.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User create(User user);

    Optional<User> findById(Long id);

    User update(User user);

    void delete(Long id);

    List<User> findAll();
}