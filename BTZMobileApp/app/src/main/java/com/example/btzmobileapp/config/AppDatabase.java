package com.example.btzmobileapp.config;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.btzmobileapp.module.user.dao.UserDao;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.equipamento.dao.EquipamentoDao; // Importar o EquipamentoDao
import com.example.btzmobileapp.module.equipamento.domain.Equipamento; // Importar a entidade Equipamento

@Database(entities = {User.class, Equipamento.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EquipamentoDao equipamentoDao(); // Adicionar o DAO de Equipamento

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}