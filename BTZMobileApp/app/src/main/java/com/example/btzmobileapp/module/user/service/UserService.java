package com.example.btzmobileapp.module.user.service;

import android.content.Context;

import com.example.btzmobileapp.config.AppDatabase;
import com.example.btzmobileapp.module.user.dao.UserDao;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.security.EncryptionUtil;

import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
    }

    public void insert(User user) throws Exception {
        User existingUserByUsername = userDao.getUserByUsername(user.getUsername());
        User existingUserByCpf = userDao.getUserByCpf(user.getCpf());

        if (existingUserByUsername != null || existingUserByCpf != null) {
            throw new Exception("Usuário já existe!");
        }
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        userDao.insert(user);
    }

    public void updateByCpf(String cpf, User user) throws Exception {
        User existingUser = userDao.getUserByCpf(cpf);
        if (existingUser == null) {
            throw new Exception("Usuário não encontrado!");
        }
        if (!existingUser.getId().equals(user.getId())) {
            User userWithSameCpf = userDao.getUserByCpf(user.getCpf());
            if (userWithSameCpf != null && !userWithSameCpf.getId().equals(existingUser.getId())) {
                throw new Exception("CPF já cadastrado para outro usuário!");
            }
        }
        user.setId(existingUser.getId());
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        userDao.update(user);
    }

    public void deleteByUsername(String username) throws Exception {
        User existingUser = userDao.getUserByUsername(username);
        if (existingUser == null) {
            throw new Exception("Usuário não encontrado!");
        }
        userDao.delete(existingUser);
    }

    public void deleteByCpf(String cpf) throws Exception {
        User existingUser = userDao.getUserByCpf(cpf);
        if (existingUser == null) {
            throw new Exception("Usuário não encontrado!");
        }
        userDao.delete(existingUser);
    }

    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByCpf(String cpf) {
        return userDao.getUserByCpf(cpf);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User validateUser(String username, String password) throws Exception {
        User user = userDao.getUserByUsername(username);
        if (user != null && EncryptionUtil.verifyPassword(password, user.getPassword())) {
            return user;
        } else {
            throw new Exception("Credenciais inválidas!");
        }
    }
}