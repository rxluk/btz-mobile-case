package com.example.btzmobileapp.module.user.service;

import android.content.Context;
import com.example.btzmobileapp.config.AppConfig;
import com.example.btzmobileapp.module.user.dao.UserDao;
import com.example.btzmobileapp.module.user.domain.User;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(Context context) {
        this.userDao = AppConfig.getDatabase(context).userDao();
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByNome(String nome) {
        return userDao.getUserByName(nome);
    }

    public void insertUser(User user) throws Exception {
        if (userDao.getUserByUsername(user.getUsername()) != null) {
            throw new Exception("Username already exists.");
        }
        userDao.insert(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public void updateUser(User user) throws Exception {
        User existingUser = userDao.getUserByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new Exception("Username already exists.");
        }
        userDao.update(user);
    }
}
