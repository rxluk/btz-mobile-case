package com.example.btzmobileapp.module.user.controller;

import android.content.Context;

import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.user.service.UserService;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController(Context context) {
        userService = new UserService(context);
    }

    public void insertUser(User user) throws Exception {
        userService.insert(user);
    }

    public void updateUser(String oldUsername, User user) throws Exception {
        userService.update(oldUsername, user);
    }

    public void deleteUser(String username) throws Exception {
        userService.delete(username);
    }

    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}