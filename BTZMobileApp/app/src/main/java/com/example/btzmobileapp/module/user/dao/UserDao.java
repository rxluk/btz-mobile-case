package com.example.btzmobileapp.module.user.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.btzmobileapp.module.user.domain.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE nome = :nome LIMIT 1")
    User getUserByName(String nome);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}
