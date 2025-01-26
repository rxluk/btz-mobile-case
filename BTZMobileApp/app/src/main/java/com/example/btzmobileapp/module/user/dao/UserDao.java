package com.example.btzmobileapp.module.user.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.btzmobileapp.module.user.domain.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    @Transaction
    void update(User user);

    @Delete
    @Transaction
    void delete(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(Long id);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE cpf = :cpf")
    User getUserByCpf(String cpf);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}