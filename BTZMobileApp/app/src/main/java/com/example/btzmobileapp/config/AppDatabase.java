package com.example.btzmobileapp.config;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.btzmobileapp.module.user.dao.UserDao;
import com.example.btzmobileapp.module.user.domain.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
