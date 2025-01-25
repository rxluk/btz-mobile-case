package com.example.btzmobileapp.module.user.controller;

import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.user.service.UserService;
import java.util.List;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public User getUserByNome(String nome) {
        return userService.getUserByNome(nome);
    }

    public void insertUser(User user) throws Exception {
        userService.insertUser(user);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void deleteUser(User user) {
        userService.deleteUser(user);
    }

    public void updateUser(User user) throws Exception {
        userService.updateUser(user);
    }
}