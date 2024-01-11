package com.example.pocketmonsters.database.room.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pocketmonsters.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);
    @Query("SELECT * FROM user")
    List<User> loadAll();
    @Query("DELETE FROM user WHERE uid = (SELECT uid FROM user ORDER BY uid LIMIT 1)")
    void clear();
    //get the uid of the first user
    @Query("SELECT uid FROM user LIMIT 1")
    int getUid();
    @Query("SELECT sid FROM user LIMIT 1")
    String getSid();

}
