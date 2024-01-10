package com.example.pocketmonsters.database.room.virtualObj;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.List;

@Dao
public interface VirtualObjDao {

    @Insert
    void insert(VirtualObj virtualObj);
    @Query("SELECT * FROM virtualObj")
    List<User> loadAll();
    @Query("SELECT * FROM virtualObj WHERE id = :id")
    VirtualObj loadById(int id);

}
