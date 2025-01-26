package com.example.btzmobileapp.module.user.service;

import android.content.Context;
import android.os.AsyncTask;

import com.example.btzmobileapp.config.AppDatabase;
import com.example.btzmobileapp.module.user.dao.UserDao;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.security.EncryptionUtil;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
    }

    public void insert(User user) throws Exception {
        User existingUser = new GetUserByUsernameAsync(userDao).execute(user.getUsername()).get();
        if (existingUser != null) {
            throw new Exception("Usuário já existe!");
        }
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        new InsertUserAsync(userDao).execute(user);
    }

    public void update(String oldUsername, User user) throws Exception {
        User existingUser = new GetUserByUsernameAsync(userDao).execute(oldUsername).get();
        if (existingUser == null) {
            throw new Exception("Usuário não encontrado!");
        }
        user.setId(existingUser.getId());
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        new UpdateUserAsync(userDao).execute(user);
    }

    public void delete(String username) throws Exception {
        User existingUser = new GetUserByUsernameAsync(userDao).execute(username).get();
        if (existingUser == null) {
            throw new Exception("Usuário não encontrado!");
        }
        new DeleteUserAsync(userDao).execute(existingUser);
    }

    public User getUserById(Long id) {
        try {
            return new GetUserByIdAsync(userDao).execute(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            return new GetUserByUsernameAsync(userDao).execute(username).get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return new GetAllUsersAsync(userDao).execute().get();
        } catch (Exception e) {
            return null;
        }
    }

    public User validateUser(String username, String password) throws Exception {
        User user = new GetUserByUsernameAsync(userDao).execute(username).get();
        if (user != null && EncryptionUtil.verifyPassword(password, user.getPassword())) {
            return user;
        } else {
            throw new Exception("Credenciais inválidas!");
        }
    }

    private static class InsertUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        InsertUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        UpdateUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        DeleteUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

    private static class GetUserByIdAsync extends AsyncTask<Long, Void, User> {
        private UserDao userDao;

        GetUserByIdAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(Long... ids) {
            return userDao.getUserById(ids[0]);
        }
    }

    private static class GetUserByUsernameAsync extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        GetUserByUsernameAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... usernames) {
            return userDao.getUserByUsername(usernames[0]);
        }
    }

    private static class GetAllUsersAsync extends AsyncTask<Void, Void, List<User>> {
        private UserDao userDao;

        GetAllUsersAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }
    }
}