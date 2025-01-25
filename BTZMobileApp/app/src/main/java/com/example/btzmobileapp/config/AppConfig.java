package com.example.btzmobileapp.config;

import android.content.Context;
import androidx.room.Room;

public class AppConfig {
    private static AppDatabase appDatabase;

    public static AppDatabase getDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "btzmobileapp-database")
                    .allowMainThreadQueries() // Evite isso em aplicativos de produção
                    .build();
        }
        return appDatabase;
    }
}
