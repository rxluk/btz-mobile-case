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

    public void updateUserByCpf(String cpf, User user) throws Exception {
        userService.updateByCpf(cpf, user);
    }

    public void deleteUserByUserName(String username) throws Exception {
        userService.deleteByUsername(username);
    }

    public void deleteUserByCpf(String cpf) throws Exception {
        userService.deleteByCpf(cpf);
    }

    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public User getUserByCpf(String cpf) {
        return userService.getUserByCpf(cpf);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}