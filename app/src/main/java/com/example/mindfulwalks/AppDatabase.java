package com.example.mindfulwalks;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Checkpoint.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract CheckpointDao checkpointDao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "mindfulwalks_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // OK for student project
                    .build();
        }

        return instance;
    }
}
