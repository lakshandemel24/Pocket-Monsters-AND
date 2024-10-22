package com.example.pocketmonsters.database.room.user;

import android.content.Context;

import androidx.room.Room;

import com.example.pocketmonsters.models.User;

public class UserDBHelper {

    private final UserRepository appDatabase;
    //private final UserRepository inMemoryAppDatabase;

    public UserDBHelper(Context context) {
        appDatabase = Room.databaseBuilder(context, UserRepository.class, "User.db")
                .allowMainThreadQueries()
                .build();
        /*
        inMemoryAppDatabase = Room.inMemoryDatabaseBuilder(context, UserRepository.class)
                .allowMainThreadQueries()
                .build();
         */
    }

    public int count() {
        return appDatabase.userDao().loadAll().size();
    }

    public void insertUser(User user) {
        appDatabase.userDao().insert(user);
    }

    public void clearUsers() {
        appDatabase.userDao().clear();
    }
    public User getUser() {
        return appDatabase.userDao().loadAll().get(0);
    }

    public int getUid() {
        return appDatabase.userDao().getUid();
    }
    public String getSid() {
        return appDatabase.userDao().getSid();
    }

}
