package com.example.pocketmonsters.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pocketmonsters.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRepository extends RoomDatabase {
    public abstract UserDao userDao();
}
