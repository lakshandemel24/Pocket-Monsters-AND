package com.example.pocketmonsters.database.room.user;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pocketmonsters.database.room.user.UserDao;
import com.example.pocketmonsters.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRepository extends RoomDatabase {
    public abstract UserDao userDao();
}
